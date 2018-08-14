package lvxixiao.dao;

import lvxixiao.bean.AuthorPo;
import lvxixiao.bean.DocumentPo;
import lvxixiao.dao.repo.DocumentRepo;
import lvxixiao.dto.DocumentDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentDaoTest {
    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private MongoOperations mongoOps;


    private AuthorPo author;

    private DocumentPo doc1;

    private DocumentPo doc2;

    private DocumentPo doc3;

    private DocumentPo doc4;

    private DocumentPo doc5;

    @Before
    public void addData(){
        LocalDateTime now = LocalDateTime.now();
        author = new AuthorPo("lvxixiao",now,now);
        author = authorDao.save(author);

        doc1 = new DocumentPo("title1","content1",author.getId(),now,now);
        doc2 = new DocumentPo("标题","内容",author.getId(),now,now);
        doc3 = new DocumentPo("t1","c1",author.getId(),now,now);
        doc4 = new DocumentPo("le1","nt1",author.getId(),now,now);
        doc5 = new DocumentPo("xxxxxx","aaaaaaaaa",author.getId(),now,now);

        doc1 = documentDao.save(doc1);
        doc2 = documentDao.save(doc2);
        doc3 = documentDao.save(doc3);
        doc4 = documentDao.save(doc4);
        doc5 = documentDao.save(doc5);
    }

    @After
    public void deleteData(){
        mongoOps.dropCollection(DocumentPo.class);
        mongoOps.dropCollection(AuthorPo.class);
    }

    @Test
    public void exactSearch(){
        var documents = documentDao.findByIdAuthor(author.getId());
        documents.forEach(document -> {
            System.out.println(document.toString());
        });
    }

    @Test
    public void fuzzSearch(){

        var documents = documentDao.findByContent("ten");
        documents.forEach(document ->{
            System.out.println(document.toString());
        });

    }

    @Test
    public void lookup(){
        System.out.println(authorDao.findById(doc1.getIdAuthor()));
        AggregationResults result = documentDao.findById(doc1.getId());
        Iterator<DocumentDto> iterator =  result.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }

    @Test
    public void pageSearch(){
        var documents = documentDao.findByIdAuthor(author.getId(),0,2);
        documents.forEach(document ->{
            System.out.println(document.toString());
        });
    }
}
