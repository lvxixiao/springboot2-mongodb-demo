package lvxixiao.dao;

import lvxixiao.bean.AuthorPo;
import lvxixiao.dao.repo.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDao {
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private MongoOperations mongoOps;

    public AuthorPo save(AuthorPo author) {
        return authorRepo.save(author);
    }

    public AuthorPo findById(String id){
        return mongoOps.findById(id,AuthorPo.class);
    }

}
