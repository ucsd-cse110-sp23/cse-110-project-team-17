

import static org.junit.Assert.*;
import org.junit.Test;

public class US2Test {
    @Test 
    public void testChatGPT() {
        ChatGPT chat = new ChatGPT();
        try {
            assertEquals("whatever", chat.returnedAnswer());
        }
        catch (Exception e) {
            System.out.println("Wrong!");
        }
    }
}
