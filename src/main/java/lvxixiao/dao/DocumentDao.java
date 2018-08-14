package lvxixiao.dao;

import com.mongodb.BasicDBObject;
import lvxixiao.bean.AuthorPo;
import lvxixiao.bean.DocumentPo;
import lvxixiao.dao.repo.DocumentRepo;
import lvxixiao.dto.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class DocumentDao {
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private MongoOperations mongoOps;
    //精确查找
    public List<DocumentPo> findByIdAuthor(String idAuthor){

        return documentRepo.findByIdAuthor(idAuthor);
    }
    //模糊查找
    public List<DocumentPo> findByContent(String content){

        return mongoOps.find(query(where("content").regex(content,"im")),DocumentPo.class);
    }
    //联表查找
    //查询其它表的字段为_id时，无法查出，但是如果查询非_id字段，则没有问题。
    public AggregationResults findById(String id){

        TypedAggregation<DocumentPo> agg = newAggregation(
                DocumentPo.class,
                match(where("_id").is(id)),
                lookup("author","idAuthor","_id","author")
        );

        AggregationResults<DocumentDto> results = mongoOps.aggregate(agg,DocumentPo.class,DocumentDto.class);

        return results;

    }
    //分页查找
    public List<DocumentPo> findByIdAuthor(String idAuthor,int page,int size) {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"title"));
        Pageable pageable = PageRequest.of(page, size,sort);
        //documentRepo.findByIdAuthor(idAuthor,pageable);
        return mongoOps.find(query(where("idAuthor").is(idAuthor)).with(pageable),DocumentPo.class);
    }

    public DocumentPo save(DocumentPo document) {
        return documentRepo.save(document);
    }


}
