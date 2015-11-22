package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by shadow on 15/10/30.
 */
public interface MessageRepo extends JpaRepository<Message, Long >{

}
