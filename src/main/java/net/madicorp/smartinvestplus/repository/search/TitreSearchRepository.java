package net.madicorp.smartinvestplus.repository.search;

import net.madicorp.smartinvestplus.domain.Titre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Titre entity.
 */
public interface TitreSearchRepository extends ElasticsearchRepository<Titre, Long> {
}
