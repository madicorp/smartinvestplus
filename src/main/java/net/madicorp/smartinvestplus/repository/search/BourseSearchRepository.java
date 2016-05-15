package net.madicorp.smartinvestplus.repository.search;

import net.madicorp.smartinvestplus.domain.Bourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bourse entity.
 */
public interface BourseSearchRepository extends ElasticsearchRepository<Bourse, Long> {
}
