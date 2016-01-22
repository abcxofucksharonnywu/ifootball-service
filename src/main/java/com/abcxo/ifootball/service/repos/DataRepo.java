package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Data;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by shadow on 15/10/30.
 */
public interface DataRepo extends JpaRepository<Data, Long> {
    Data findByNameAndCategory(String name,String category);
}
