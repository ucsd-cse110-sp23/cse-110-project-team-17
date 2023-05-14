## SayIt Assistant Milestone 1

Instructions:
1. Unzip the SayIt Assistant zip file
2. Open a terminal or command prompt
2. Navigate to src/main/java
3. For Windows users run:
    1. javac -cp ../../../libs/json-20230227.jar;. project/SayItAssistant.java
    2. java -cp ../../../libs/json-20230227.jar;. project/SayItAssistant
4. For Linux Users run:
    1. javac -cp ../../../libs/json-20230227.jar:. project/SayItAssistant.java
    2. java -cp ../../../libs/json-20230227.jar:. project/SayItAssistant

# Welcome to the SayIt Assistant!
- In the GUI the history window on the left contains previous questions asked
    - The Clear All button clears all previous questions asked, leaving a clean slate
    - The Delete Selected button clears the specific question selected
        - To select a question, find the specific question desired in the history window and click the select button to the right of that question
- The Right window contains the chatbox where SayIt Assistant will give the user answers to their questions
- To ask a question: 
    - Click the Ask a Question button in the bottom left
    - Speak your question
    - Click the Stop Recording button that shows up in the bottom right
    - SayIt Assistant will display your question and the answer to it in the right chatbox window