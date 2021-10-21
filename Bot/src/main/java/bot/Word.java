package bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class Word {
    private String word;
    private String translate;

    public Word(String word, String translate){
        this.word = word;
        this.translate = translate;
    }

}
