package com.nomad.data.agent.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Paths;

@Component
@Slf4j
public class DockerUtils {

    @Inject
    Environment env;

    public DockerClientConfig DockerUtils() {
        String dockerRegistryUrl = env.getProperty("docker.registry");
        try {
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("unix:///var/run/docker.sock")
                    .withRegistryUrl(dockerRegistryUrl)
                    .build();
            return config;
//            this.dockerClient = DockerClientBuilder.getInstance(config).build();
        } catch (Exception e) {
            log.error(">>>>> dockerClient initializing ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }


    public void loadImage(String imageName) {
        log.debug(">>>>> docker utils load image start");
        log.debug(">>>>> {}", getImageTarPath(imageName));
//        File file  =  new File(getImageTarPath(imageName));

        try {
            CommandUtils.run(String.format("docker load < %s", this.getImageTarPath(imageName)));

        } catch (Exception e) {
            log.error(">>>>> docker loadImage exception ", e);
            throw new CustomException(ErrorCodeType.COMMON_COMMAND_RUN_FAIL);
        }

//        file.delete();
        log.debug(">>>>> docker utils load image end");
    }

    public void tagImage(String loadedImageInfo, String imageName, String imageTag){
        log.debug(">>>>> docker utils tag image start");

        try {
            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.debug(imageName + ":" + imageTag);
            log.debug(env.getProperty("docker.registry") + "/" + imageName);
            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            DockerClient dockerClient = DockerClientBuilder.getInstance(DockerUtils()).build();
            dockerClient.tagImageCmd(loadedImageInfo, env.getProperty("docker.registry") + "/" + imageName, imageTag).exec();
            dockerClient.close();

        } catch (Exception e) {
            log.error(">>>>> dockerClient tag Image ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
        log.debug(">>>>> docker utils tag image end");
    }

    public void saveImage(String imageName, String imageTag) {
        try (OutputStream output = new FileOutputStream(new File(getImageTarPath(imageName)))){

            DockerClient dockerClient = DockerClientBuilder.getInstance(DockerUtils()).build();
            IOUtils.copy(dockerClient.saveImageCmd(imageName + ":" + imageTag).exec(), output);
            dockerClient.close();

            IOUtils.closeQuietly(output);
        } catch (IOException e) {
            log.error(">>>>> dockerClient saveImage ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    public void pushImage(String imageName, String imageTag) {
        try {
            DockerClient dockerClient = DockerClientBuilder.getInstance(DockerUtils()).build();
            PushImageCmd pushImageCmd = dockerClient.pushImageCmd(getRegistryImageName(imageName, imageTag));

            PushImageResultCallback callback = new PushImageResultCallback() {
                @Override
                public void onNext(PushResponseItem item) {
                    super.onNext(item);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                }
            };

            pushImageCmd.exec(callback).awaitCompletion();
            dockerClient.close();

        } catch (Exception e) {
            log.error(">>>>> dockerClient pushImage ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    public void pullImage(String imageName, String imageTag) {
        try {
            DockerClient dockerClient = DockerClientBuilder.getInstance(DockerUtils()).build();
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(getRegistryImageName(imageName, imageTag));

            PullImageResultCallback callback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    super.onNext(item);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                }
            };
            pullImageCmd.exec(callback).awaitCompletion();
            dockerClient.close();
        } catch (Exception e) {
            log.error(">>>>> dockerClient pullImage ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    public void deleteImage(String imageName, String imageTag) {
        try {
            DockerClient dockerClient = DockerClientBuilder.getInstance(DockerUtils()).build();
            dockerClient.removeImageCmd(String.format("%s:%s", imageName, imageTag)).withNoPrune(true).exec();
            dockerClient.close();

        } catch (Exception e) {
            log.error(">>>>> dockerClient deleteImage ApiException ", e);
            throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
        }
    }

    public String getImageTarPath(String imageName) {
        return Paths.get(env.getProperty("docker.image.custom.tar.path"),imageName + ".tar").toString();
    }

    private String getRegistryImageName(String imageName, String imageTag) {
        return env.getProperty("docker.registry") + "/" + imageName + ":" + imageTag;
    }

}
