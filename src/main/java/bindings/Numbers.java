package bindings;

import lombok.*;

@EqualsAndHashCode()
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(staticName="of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Numbers{
    private Integer[] numbers;
}
