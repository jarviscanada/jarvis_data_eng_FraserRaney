package ca.jrvs.apps.trading.data.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class Position {


  @EmbeddedId
  private PositionKey id;

  private Long position;

  public Position() {
  }

  public Position(PositionKey key, Long position) {
    this.id = key;
    this.position = position;
  }

  public long getPosition() {
    return position;
  }

  public PositionKey getId() {
    return id;
  }
}
