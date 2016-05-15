package net.madicorp.smartinvestplus.repository.search;

import net.madicorp.smartinvestplus.domain.Indice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Indice entity.
 */
public interface IndiceSearchRepository extends ElasticsearchRepository<Indice, Long> {
}
