package bot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.beans.ConstructorProperties;

@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@Jacksonized
public class Word {
    private String word;
    private String translate;
//    @ConstructorProperties({"word", "translate"})
//    public Word(String word, String translate){
//        this.word = word;
//        this.translate = translate;
//    }
    @JsonCreator
    public Word(@JsonProperty("word") String word, @JsonProperty("translate") String translate) {
        this.word = word;
        this.translate = translate;
    }
}

