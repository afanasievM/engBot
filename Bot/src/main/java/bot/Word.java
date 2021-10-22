package bot;

import lombok.*;

@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Word {
    private String word;
    private String translate;

    public Word(String word, String translate){
        this.word = word;
        this.translate = translate;
    }

}
