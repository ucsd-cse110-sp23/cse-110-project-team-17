package project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
public class US4Test {
    @Test
    void testClearAllButtons() throws IOException {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new MockChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        ChatList chatList = testFrame.getChatList();
        HistoryList historyList = new HistoryList();

        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox question1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        historyList.add(answer1);
        historyList.add(question1);
        assertTrue(!historyList.getEmpty());
        historyList.removeEverything();
        assertTrue(historyList.getEmpty());
        assertTrue(0 == historyList.getComponentsNum());
    }
}
