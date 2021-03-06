package com.nomad.data.agent.common.service.docker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.common.dto.AipHttpHeaders;
import com.nomad.data.agent.common.dto.req.AipDockerImageCreateReq;
import com.nomad.data.agent.common.dto.req.AipDockerImageDeleteReq;
import com.nomad.data.agent.common.dto.req.AipDockerRegistryImageDeleteReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.AipDockerImage;
import com.nomad.data.agent.domain.dao.common.AipDockerImageExample;
import com.nomad.data.agent.domain.dao.common.AipDockerImageKey;
import com.nomad.data.agent.domain.dao.common.AipImgLibMap;
import com.nomad.data.agent.domain.dao.common.AipImgLibMapExample;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.dao.common.AipWorkspaceExample;
import com.nomad.data.agent.domain.mappers.common.AipDockerImageMapper;
import com.nomad.data.agent.domain.mappers.common.AipImgLibMapMapper;
import com.nomad.data.agent.domain.mappers.common.AipWorkspaceMapper;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.CommandUtils;
import com.nomad.data.agent.utils.CompressUtils;
import com.nomad.data.agent.utils.DateUtils;
import com.nomad.data.agent.utils.DockerUtils;
import com.nomad.data.agent.utils.FileUtils;
import com.nomad.data.agent.utils.RestApiUtils;
import com.nomad.data.agent.utils.enums.AgentApiType;
import com.nomad.data.agent.utils.enums.CodeEditorType;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.FlagType;
import com.nomad.data.agent.utils.enums.ImageType;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DockerImageServiceImpl implements DockerImageService {

    @Autowired
    DockerUtils dockerUtils;

    @Autowired
    CompressUtils compressUtils;

    @Autowired
    AipDockerImageMapper aipDockerImageMapper;

    @Autowired
    AipImgLibMapMapper aipImgLibMapMapper;

    @Autowired
    AipWorkspaceMapper aipWorkspaceMapper;

    @Autowired
    LogService logService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Value("${dapldt.ip}")
    String dockerRegistryIp;

    @Value("${daplwk1p.ip}")
    String daplwk1pIp;

    @Value("${daplwk2p.ip}")
    String daplwk2pIp;

    @Value("${agent.daplwk1p.port}")
    String daplwk1pPort;

    @Value("${agent.daplwk2p.port}")
    String daplwk2pPort;

    @Value("${docker.registry.port}")
    String dockerRegistryPort;

    @Value("${docker.registry.path}")
    String dockerRegistryPath;

    @Value("${docker.image.custom.tar.path}")
    String dockerImageCustomTarPath;

    /**
     * ?????? ?????????????????? ???????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.08
     *
     * @param user ?????????
     * @param req ????????????????????? ??????
     * @return ??????????????? ?????? ??????(???????????????)
     */
    @Override
    public void deleteRegistryImage(AipUser user, AipDockerRegistryImageDeleteReq req) {
        log.debug(">>>>> delete registry image start");

        String dockerImageId = req.getImgId();
        AipDockerImage aipDockerImage = read(dockerImageId);

        if (ObjectUtils.isEmpty(aipDockerImage)){
            log.error(">>>>> aipdockerimage is empty");
            logService.insertLog(user, LogType.DOCKER, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
            throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
        }

        String dockerImageName = aipDockerImage.getDckrImgNm();
        String dockerImageTag = aipDockerImage.getDckrImgTag();

        log.info(">>>>> docker image name :{}", dockerImageName);
        log.info(">>>>> docker image tag :{}", dockerImageTag);

        try {
            this.deletePulledImageOnWorkerServer(dockerImageId, dockerImageName, dockerImageTag);

            String dockerImageDigest = this.getDockerImageId(dockerImageName, dockerImageTag);

            this.deleteRegistryImageByDockerApi(dockerImageName, dockerImageDigest);

            this.execRegistryGarbageCollect();

            TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

            try {
                this.deleteImageLibMapInfo(dockerImageId);
                this.deleteDockerImageInfo(dockerImageName, dockerImageTag);

                logService.insertLog(user, LogType.DOCKER, LogMessageType.OK_DB_DELETE);

                transactionManager.commit(transactionStatus);

            } catch (Exception e) {
                log.error(">>>>> delete docker image database error", e);
                transactionManager.rollback(transactionStatus);
                logService.insertLog(user, LogType.DOCKER, LogMessageType.ERROR_DB_DELETE_HANDLE);
            }

        } catch (Exception e) {
            log.error(">>>>> delete registry image error", e);
            throw new CustomException(e.getMessage(), ErrorCodeType.DOCKER_IMAGE_DELETE_ERROR);
        }
    }

    /**
     * ????????????????????? ????????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.05.08
     *
     */
    private void execRegistryGarbageCollect(){
        CommandUtils.run("docker exec -it docker-registry_registry_1 bin/registry garbage-collect  /etc/docker/registry/config.yml");
    }

    /**
     * ??????????????? ?????? ????????? - aip_img_lib_map
     * ?????? ????????? ?????? ??????.
     *
     * @author eunbeelee
     * @date 2020.04.29
     *
     * @param dockerImageId ????????????????????????
     * @return ?????? ?????? ??????
     */
    private Integer deleteImageLibMapInfo(String dockerImageId) {
        AipImgLibMapExample example = new AipImgLibMapExample();
        example.createCriteria().andImgIdEqualTo(dockerImageId);

        if (ObjectUtils.isEmpty(example)){
            log.error(">>>>> image lib map example error");
            throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
        }

        return aipImgLibMapMapper.deleteByExample(example);
    }

    /**
     * ??????????????? tar ????????? ????????????
     * ?????? ?????????????????? ???????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.10
     *
     * @param user ?????????
     * @param req ????????????????????? ??????
     * @retrn ??????????????????????????? ??????
     */
    @Override
    public AipDockerImageKey create(AipUser user, AipDockerImageCreateReq req) {
        log.debug(">>>>> create registry image start");
        log.debug(">>>>> docker image create req:{}", req.toString());

        String dockerImageName = req.getDckrImgNm();
        String dockerImageTag = req.getDckrImgTag();

        String fileOriginalFilename = req.getDckrImgFileNm().replace(".tar", "");
        
        try {
        	this.checkDuplicateDockerImage(dockerImageName, dockerImageTag);
        }catch(Exception e) {
        	log.error(e.getMessage(), e);
        	throw e;
        }
        
        String loadedDockerImageInfo = this.getDockerImageRepoTag(req.getDckrImgFileNm());
        
        log.info(">>>>> file load end");
        log.info(">>>>> {}", loadedDockerImageInfo);
        
        try {

            log.debug(">>>>> get docker image load start");
            this.loadDockerImage(fileOriginalFilename);
            log.debug(">>>>> get docker image load end");

            log.debug(">>>>> get docker image tag start");
            this.tagDockerImage(loadedDockerImageInfo, dockerImageName, dockerImageTag);
            log.debug(">>>>> get docker image tag end");

            log.debug(">>>>> get docker image push start");
            this.pushDockerImageToRegistry(dockerImageName, dockerImageTag);
            log.debug(">>>>> get docker image push end");

            log.debug(">>>>> get docker image delete start");
            this.deleteDockerImage(loadedDockerImageInfo.split(":")[0], loadedDockerImageInfo.split(":")[1]);
            this.deleteDockerImage(this.getRegistryImageName(dockerImageName), dockerImageTag);
            log.debug(">>>>> get docker image delete end");

            String dockerImageId = DateUtils.getSystemId();
            Integer dockerImageResult = this.insertDockerImageInfo(user, req, dockerImageId);

            Integer dockerImageLibMapResult = this.insertImageLibMapInfo(dockerImageId);

            if (dockerImageResult > 0 && dockerImageLibMapResult > 0) {
                logService.insertLog(user, LogType.DOCKER, LogMessageType.OK_DB_INSERT);
                return getKey(dockerImageId);
            }
            throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);

        } catch (Exception e) {
            log.error(">>>>> load docker image error", e);
            throw new CustomException(ErrorCodeType.DOCKER_IMAGE_REGISTER_ERROR);
        }
    }

    @Override
    public List<String> list() {
    	List<String> dockerFileList = new ArrayList<>();
    	
    	String[] fileList = FileUtils.listFiles(dockerImageCustomTarPath);
    	if(fileList == null) {
    		return null;
    	}
    	for(String file : fileList) {
    		if(file.contains(".tar")) {
    			dockerFileList.add(file);
    		}
    	}
    	return dockerFileList;
    }
    
    /**
     * ??????????????? ????????????
     * ?????? ???????????? ????????????. (aip_img_lib_map)
     *
     * @author eunbeelee
     * @date 2020.04.29
     *
     * @param dockerImageId ????????????????????????
     * @return db insert ?????? ???
     */
    private Integer insertImageLibMapInfo(String dockerImageId) {
        AipImgLibMap aipImgLibMap = new AipImgLibMap();

        aipImgLibMap.setImgId(dockerImageId);
        aipImgLibMap.setLibSmlCd(CodeEditorType.CST.getValue());
        aipImgLibMap.setRegDt(DateUtils.getNow());
        aipImgLibMap.setModDt(DateUtils.getNow());

        return aipImgLibMapMapper.insert(aipImgLibMap);
    }

    /**
     * ???????????? ??????????????? ????????? ???????????? ???
     * db ????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.29
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     */
    private void checkDuplicateDockerImage(String dockerImageName, String dockerImageTag) {
        AipDockerImageExample aipDockerImageExample = new AipDockerImageExample();
        aipDockerImageExample.createCriteria().andDckrImgNmEqualTo(dockerImageName).andDckrImgTagEqualTo(dockerImageTag).andDelFlEqualTo(FlagType.FALSE.getValue());

        List<AipDockerImage> result  = aipDockerImageMapper.selectByExample(aipDockerImageExample);

        if (! ObjectUtils.isEmpty(result)){
            throw new CustomException(ErrorCodeType.DOCKER_IMAGE_DUPLICATE_ERROR);
        }
    }

    /**
     * ?????? ????????? ????????? ????????????.
     * (tar ?????? ?????? ??????)
     *
     * @author eunbeelee
     * @date 2020.04.08
     *
     * @param file
     * @return file ????????? ????????? ????????????
     */
    private String checkFileValidation(MultipartFile file) {
        String fileOriginalFilename = file.getOriginalFilename();

        if (!fileOriginalFilename.contains(".tar")){
            throw new CustomException(ErrorCodeType.FILE_INVALID_TYPE);
        }
        return fileOriginalFilename.replace(".tar", "");
    }

    /**
     * ??????????????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.08
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     *
     * @return ???????????????????????? (ex :  sha256:2018287052387a5ba149ec7310c2289e3c2385bba08f4ef8ca043ba8201d0e60)
     */
    private String getDockerImageId(String dockerImageName, String dockerImageTag){
        log.debug(">>>>> get docker image id start");

        String apiType = String.format("/v2/%s/manifests/%s", dockerImageName, dockerImageTag);
        String uri = RestApiUtils.makeUri(dockerRegistryIp, dockerRegistryPort, apiType);

        log.info(">>>>> docker api uri:{}", uri);

        ResponseEntity responseEntity = null;

        try {
            AipHttpHeaders httpHeaders = new AipHttpHeaders();
            httpHeaders.setAcceptType(new MediaType("application", "vnd.docker.distribution.manifest.v2+json"));

            responseEntity = (ResponseEntity) RestApiUtils.callApiAll(uri, null, HttpMethod.GET, httpHeaders);

        } catch (Exception e) {
            log.error(">>>>> get docker image id error", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }

        return responseEntity.getHeaders().getValuesAsList("Docker-Content-Digest").get(0);
    }

    /**
     * docker api??? ?????????
     * ?????????????????? ?????? ???????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.08
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageDigest ????????????????????????
     */
    private void deleteRegistryImageByDockerApi(String dockerImageName, String dockerImageDigest){
        log.debug(">>>>> delete registry image by docker api start");

        String apiType = String.format("/v2/%s/manifests/%s", dockerImageName, dockerImageDigest);

        String uri = RestApiUtils.makeUri(dockerRegistryIp, dockerRegistryPort, apiType);

        log.info(">>>>> delete docker image api uri:{}", uri);

        try {
            AipHttpHeaders httpHeaders = new AipHttpHeaders();
            httpHeaders.setAcceptType(new MediaType("application", "vnd.docker.distribution.manifest.v2+json"));

            RestApiUtils.callApiAll(uri, null, HttpMethod.DELETE, httpHeaders);

        } catch (Exception e) {
            log.error(">>>>> delete registry image by docker api error", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    /**
     * ???????????? ????????????
     * ????????????????????????????????? ??????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.08
     *
     * @param dockerImageName ?????????????????????
     */
    private void deleteRegistryImageOnMountedDirectory(String dockerImageName, String dockerImageDigest){
        log.debug(">>>>> delete registry image on mounted directory start");

        Path dockerImagePath = Paths.get(dockerRegistryPath, dockerImageName);
        log.debug(">>>>> docker image path :{}", dockerImagePath);

        if (!dockerImagePath.toFile().exists()){
            log.error(">>>>> docker image path not found");
            throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
        }
        Path digestPath = Paths.get(dockerImagePath.toString(), "_manifests/revisions/sha256");
        log.info(">>>>> digest path:{}", digestPath);
        File[] digestsList = digestPath.toFile().listFiles();

        File deleteTarget = null;
        if (digestsList.length == 1){
            deleteTarget = dockerImagePath.toFile();
        }
        if (digestsList.length > 1){
            deleteTarget = Paths.get(digestPath.toString(), dockerImageDigest).toFile();
        }
        try {
            FileUtils.deleteDirectory(deleteTarget);

        } catch (Exception e) {
            log.error(">>>>> delete registry image on mounted directory error", e);
            throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
        }


    }

    /**
     * ???????????? ??????????????? ????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param file ?????????????????????
     *
     */
    private String uploadDockerImageFile(MultipartFile file){
        log.debug(">>>>> upload docker image file start");

        if (ObjectUtils.isEmpty(file)){
            throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
        }
        
        try {
            FileUtils.uploadFile(file, Paths.get(dockerImageCustomTarPath));
        }catch(Exception e) {
        	log.error(e.getMessage(), e);
        	throw e;
        }
        
        try {
            String repoTag = this.getDockerImageRepoTag(file.getOriginalFilename());
            if (StringUtils.isEmpty(repoTag)){
                throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
            }
            return repoTag;

        }catch (Exception e){
            log.error(">>>>> get docker image repoTag error", e);
            throw new CustomException(ErrorCodeType.DOCKER_IMAGE_INVALID_ERROR);
        }
    }

    /**
     * ????????? ?????? ????????????
     * ????????? manifest.json ?????? ????????? ?????? ??????
     * repoTag ????????? ????????????.
     *
     * @param originalFilename ?????? ??????(???????????? ??????)
     * @return ??????????????? repoTag
     */
    private String getDockerImageRepoTag(String originalFilename) {
        String sourceFile = Paths.get(dockerImageCustomTarPath, originalFilename).toString();
        String fileName = originalFilename.replace(".tar", "");
        String target = Paths.get(dockerImageCustomTarPath, fileName).toString();


        log.info(sourceFile);
        if (!new File(sourceFile).exists()){
            log.error(">>>>>"+ ErrorCodeType.FILE_NOT_FOUND.getMessage());
            throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
        }
        try {
            compressUtils.decompress(sourceFile, target);
        } catch (IOException e) {
            log.error(">>>>> file decompress error", e);
            throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
        }

        File manifestFile = Paths.get(dockerImageCustomTarPath, fileName, "manifest.json").toFile();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(manifestFile))){
            String line = "";
            while ((line = fileReader.readLine()) != null){
                String[] infoElements = line.split(",");
                for (String element : infoElements) {
                    if (element.contains("RepoTags")){
                         String repoTag = element.substring(element.indexOf("[")+1, element.indexOf("]")).replaceAll("\"", "");
                         FileUtils.deleteDirectory(new File(target));
                         return repoTag;
                    }
                }
            }
        } catch (Exception e) {
            log.error(">>>>> get docker image tag error", e);
//            FileUtils.deleteDirectory(new File(target));
            throw new CustomException(ErrorCodeType.DOCKER_IMAGE_INVALID_ERROR);
        }
        return null;
    }

    /**
     * ??????????????????????????? ????????? tar ????????? ????????????
     * ?????????????????? ?????????
     *
     * @author eunbeelee
     * @date 2020.04.10
     *
     * @param dockerImageName ?????????????????????
     */
    private void loadDockerImage(String dockerImageName){
        log.debug(">>>>> load docker image start");
        dockerUtils.loadImage(dockerImageName);
        log.debug(">>>>> load docker image end");
    }

    /**
     * ??????????????? tag??? ??????
     * ????????? ????????? ??????.
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     */
    private void tagDockerImage(String loadedImageInfo, String dockerImageName, String dockerImageTag){
        log.debug(">>>>> tag docker image start");

        try {
            dockerUtils.tagImage(loadedImageInfo, dockerImageName, dockerImageTag);
            log.debug(">>>>> tag docker image end");

        } catch (Exception e) {
            log.error(">>>>> tag docker image error", e);
//            dockerUtils.deleteImage(dockerImageName, dockerImageTag);

            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    /**
     * ?????????????????? ?????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.10
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     */
    private void pushDockerImageToRegistry(String dockerImageName, String dockerImageTag){
        log.debug(">>>>> push docker image start");

        try {
            dockerUtils.pushImage(dockerImageName, dockerImageTag);

        } catch (Exception e) {
            log.error(">>>>> push docker image error", e);
            dockerUtils.deleteImage(this.getRegistryImageName(dockerImageName), dockerImageTag);

            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    /**
     * ?????? ?????????????????? ????????? ???????????? ?????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param dockerImageName
     * @param dockerImageTag
     */
    private void deleteDockerImage(String dockerImageName, String dockerImageTag){
        log.debug(">>>>> delete docker image start");

        try {
            dockerUtils.deleteImage(dockerImageName, dockerImageTag);

        } catch (Exception e) {
            log.error(">>>>> delete docker image error", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    /**
     * ????????? ?????????????????? ????????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param req ????????????????????? ??????
     */
    private int insertDockerImageInfo(AipUser user, AipDockerImageCreateReq req, String imageId){
        log.debug(">>>>> insert docker image start");
        log.debug(">>>>> docker image create req{}", req.toString());

        AipDockerImage aipDockerImage = req.toDao();

        aipDockerImage.setImgId(imageId);
        aipDockerImage.setImgTp(ImageType.CUSTOM.getValue());
        aipDockerImage.setRegId(user.getUserId());
        aipDockerImage.setDelFl(FlagType.FALSE.getValue());
        Date now = DateUtils.getNow();
        aipDockerImage.setRegDt(now);
        aipDockerImage.setModDt(now);

        try {
             return aipDockerImageMapper.insertSelective(aipDockerImage);

        } catch (Exception e) {
            log.error(">>>>> insert docker image info error", e);

            String dockerImageName = req.getDckrImgNm();
            String dockerImageTag = req.getDckrImgTag();

            String dockerImageId = this.getDockerImageId(dockerImageName, dockerImageTag);

            this.deleteRegistryImageByDockerApi(dockerImageName, dockerImageId);

            this.deleteRegistryImageOnMountedDirectory(dockerImageName, dockerImageId);

            throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);
        }
    }

    /**
     * ????????? docker registry??????
     * worker server??? pull ??????
     * ?????????????????? ????????????.
     *
     * ?????? worker server ??????
     * ?????? ????????? ?????????????????? ????????? ???????????? ?????? ?????? ????????????.
     * - work server api ??????
     *
     * @author eunbeelee
     * @date 2020-04-14
     *
     * @param dockerImageId ????????????????????????
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     */
    private void deletePulledImageOnWorkerServer(String dockerImageId, String dockerImageName, String dockerImageTag){
        log.debug(">>>>> delete pulled image on worker server start");

        if (this.existInUseWorkspaceByImage(dockerImageId)){
            throw new CustomException(ErrorCodeType.WORKSPACE_EXIST);
        }

        AipDockerImageDeleteReq aipDockerImageDeleteReq = new AipDockerImageDeleteReq();
        aipDockerImageDeleteReq.setDockerImageName(dockerImageName);
        aipDockerImageDeleteReq.setDockerImageTag(dockerImageTag);

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            Future futureWk1p = RestApiUtils.callApiRunnableThread(
                    RestApiUtils.makeUri(daplwk1pIp, daplwk1pPort, AgentApiType.WORKER_DOCKER_IMAGE_DELETE),
                    aipDockerImageDeleteReq,
                    HttpMethod.POST,
                    null,
                    executorService);

            Future futureWk2p = RestApiUtils.callApiRunnableThread(
                    RestApiUtils.makeUri(daplwk2pIp, daplwk2pPort, AgentApiType.WORKER_DOCKER_IMAGE_DELETE),
                    aipDockerImageDeleteReq,
                    HttpMethod.POST,
                    null,
                    executorService);

            futureWk1p.get(10, TimeUnit.MINUTES);
            futureWk2p.get(10, TimeUnit.MINUTES);

            executorService.shutdown();

        } catch (Exception e) {
            log.error(">>>>> delete pulled image on worker server error:", e);
            throw new CustomException(ErrorCodeType.API_CALL_ERROR_WRONG);
        }
    }

    /**
     * ??????????????????????????? ????????????
     * ????????????????????? ??????????????? ????????????
     * ??????????????? true, ???????????? ???????????? false??? ????????????.
     *
     * @author eunbeelee
     * @date 2020-04-29
     *
     * @param dockerImageId ????????????????????????
     * @return ????????? ???????????? ?????????????????? ?????? ??????
     */
    private boolean existInUseWorkspaceByImage(String dockerImageId) {
        AipWorkspaceExample workspaceExample = new AipWorkspaceExample();

        workspaceExample.createCriteria()
                .andImgIdEqualTo(dockerImageId)
                .andDelFlEqualTo(FlagType.FALSE.getValue());

        long workspaceCount = aipWorkspaceMapper.countByExample(workspaceExample);

        return workspaceCount > 0? true : false;
    }

    /**
     * ????????? ???????????????
     * ?????????????????? DEL_FL ????????????
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param dockerImageName ?????????????????????
     * @param dockerImageTag ?????????????????????
     */
    private int deleteDockerImageInfo(String dockerImageName, String dockerImageTag){
        log.debug(">>>>> delete docker image info start");

        AipDockerImageExample aipDockerImageExample = new AipDockerImageExample();
        aipDockerImageExample.createCriteria().andDckrImgNmEqualTo(dockerImageName).andDckrImgTagEqualTo(dockerImageTag);

        AipDockerImage record = new AipDockerImage();
        record.setDelFl(FlagType.TRUE.getValue());
        record.setModDt(DateUtils.getNow());

        try {
            return aipDockerImageMapper.updateByExampleSelective(record, aipDockerImageExample);

        }catch (Exception e){
            log.error(">>>>> docker image update error", e);
            throw new CustomException(ErrorCodeType.DB_UPDATE_HANDLE);
        }
    }

    /**
     * ??????????????? ????????????
     * ?????????????????? ?????? ?????????????????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param dockerImageId ????????????????????????
     * @return ???????????????
     */
    private AipDockerImage read(String dockerImageId){
        AipDockerImageKey key = new AipDockerImageKey();
        key.setImgId(dockerImageId);

        return aipDockerImageMapper.selectByPrimaryKey(key);
    }

    /**
     * ????????????????????? ???????????? ??????????????? ?????? ????????????.
     *
     * @author eunbeelee
     * @date 2020.04.16
     *
     * @param dockerImageId
     * @return ??????????????????
     */
    private AipDockerImageKey getKey(String dockerImageId) {
        AipDockerImageKey key = new AipDockerImageKey();
        key.setImgId(dockerImageId);
        return key;
    }

    /**
     * ????????????????????? ?????? ????????????????????? ????????? ?????????.
     * ex) 192.342.43.15:3666/{dockerImageName}
     *
     * @author eunbeelee
     * @date 2020.04.17
     *
     * @param dockerImageName  ?????????????????????
     * @return ????????????????????? ????????? ????????? ?????????
     */
    private String getRegistryImageName(String dockerImageName) {
        return String.format("%s:%s/%s", dockerRegistryIp, dockerRegistryPort, dockerImageName);
    }
}

