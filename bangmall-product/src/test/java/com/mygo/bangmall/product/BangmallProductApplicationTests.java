package com.mygo.bangmall.product;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.mygo.bangmall.product.entity.BrandEntity;
import com.mygo.bangmall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;


@RunWith(SpringRunner.class)

@SpringBootTest()
class BangmallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Resource
    private OSSClient ossClient;
    @Test
    public void testUpload() {
        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        PutObjectRequest putObjectRequest = new PutObjectRequest("bangmall", "122538084_p3_master1200.jpg", new File("C:\\Users\\TEMPEST\\Pictures\\Saved Pictures\\122538084_p3_master1200.jpg"));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("苹果");
        brandService.save(brandEntity);
    }

}
