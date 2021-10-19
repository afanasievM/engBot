package bot;

import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {
    private Chat chat;
    private String role;
    private Integer repeats = 10;
    private HashMap<HashMap<String,String>, Integer> volabulary;
    private Long userID;

    public User(Chat chat){
        this.chat = chat;
        this.role = "user";
        this.volabulary = new HashMap<>();
    }

    public Chat getChat() {
        return chat;
    }

    public String getRole() {
        return role;
    }

    public void addWord(HashMap<String,String> word){
        this.volabulary.put(word,this.repeats);
    }

    public void addWord(String word, String translate) {
        HashMap<String,String> wordMap  = new HashMap<>();
        wordMap.put(word, translate);
        addWord(wordMap);
    }

    public void delWord(HashMap<String,String> word){
        if (volabulary.containsKey(word)) {
            this.volabulary.remove(word);
        }
    }
    public void delWord(String word){
        HashMap<String,String> wordForDelete = null;
        for (Map.Entry<HashMap<String,String>, Integer> entry: this.volabulary.entrySet()) {
            if (entry.getKey().containsKey(word)){
                wordForDelete = entry.getKey();
                break;
            }
        }
        if (wordForDelete!=null) delWord(wordForDelete);
    }

    @Override
    public String toString(){
        return this.getClass().getName() + "(role=" + this.role +", " + this.chat.toString() + ", volabulary=" + this.volabulary.toString() +")";
    }


}
