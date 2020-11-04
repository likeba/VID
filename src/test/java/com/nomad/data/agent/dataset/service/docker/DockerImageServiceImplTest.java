package com.nomad.data.agent.dataset.service.docker;

import com.nomad.data.agent.common.service.docker.DockerImageService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DockerImageServiceImplTest {

    @Autowired
    DockerImageService dockerImageService;

    static String dockerImageId;
    static String dockerImageName;
    static String dockerImageTag;

    @Before
    public void setDockerInfo(){
        dockerImageName = "hello-world";
        dockerImageTag = "1.1";
    }

    @Test
    public void test1_getDockerImageId(){
        String dockerImageId = ReflectionTestUtils.invokeMethod(dockerImageService, "getDockerImageId", dockerImageName, dockerImageTag);

        this.dockerImageId = dockerImageId;
    }

    @Test
    public void test2_deleteRegistryImageByDockerApi(){
        ReflectionTestUtils.invokeMethod(dockerImageService, "deleteRegistryImageByDockerApi", dockerImageName, dockerImageId);


    }
    @Test
    public void test3_deleteRegistryImageOnMountedDirectory(){
        ReflectionTestUtils.invokeMethod(dockerImageService, "deleteRegistryImageOnMountedDirectory", dockerImageName);
    }
}