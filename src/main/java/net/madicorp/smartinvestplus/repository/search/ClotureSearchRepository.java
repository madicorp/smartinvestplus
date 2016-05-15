package net.madicorp.smartinvestplus.repository.search;

import net.madicorp.smartinvestplus.domain.Cloture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cloture entity.
 */
public interface ClotureSearchRepository extends ElasticsearchRepository<Cloture, Long> {
}
