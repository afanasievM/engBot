package bot;

import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashMap;

public class User {
    private Chat chat;
    private String role;
    private HashMap<HashMap<String,String>, Integer> volabulary;
    private Long userID;
    final private int repeats = 10;

    public User(Chat chat){
        this.chat = chat;
        this.role = "user";
//        this.volabulary = new HashMap<>();
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

    @Override
    public String toString(){
        return this.getClass().getName() + "(role=" + this.role +", " + this.chat.toString() + ", volabulary=" + this.volabulary +")";
    }
}
