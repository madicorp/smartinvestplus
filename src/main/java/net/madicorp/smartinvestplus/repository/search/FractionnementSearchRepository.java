package net.madicorp.smartinvestplus.repository.search;

import net.madicorp.smartinvestplus.domain.Fractionnement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fractionnement entity.
 */
public interface FractionnementSearchRepository extends ElasticsearchRepository<Fractionnement, Long> {
}
