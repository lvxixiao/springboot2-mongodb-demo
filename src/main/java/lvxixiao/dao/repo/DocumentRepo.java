package lvxixiao.dao.repo;

import lvxixiao.bean.DocumentPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface DocumentRepo extends MongoRepository<DocumentPo,String> {

    List<DocumentPo> findByIdAuthor(String idAuthor);

    List<DocumentPo> findByIdAuthor(String idAuthor, Pageable pageable);
}
