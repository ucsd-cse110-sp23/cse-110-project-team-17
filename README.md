# SayIt Assistant Milestone 2

Instructions:
1. Unzip the SayIt Assistant zip file
2. Open a terminal or command prompt
2. Navigate to src/main/java
3. Use the command "gradle run"

## Welcome to the SayIt Assistant!
### Login Window
- There are 2 fields for users to input their username and password for SayIt Assistant 2
    - Select "Create Account" to create a new account usering this username and password
    - Select "Log In" to log in using this username and password
- An additional window will appear asking if the user wants their login information saved on this computer

### Main Window
- In the GUI the history window on the left contains previous questions asked
    - Previous questions asked on this account will appear in the history window, including questions asked on other devices
    - To select a prompt, find the specific prompt desired in the history window and click the select button to the right of that prompt
        - Selecting a prompt shows the prompt and response in the chatbox window and selectes it for deletion if the "Delete" command is given (described below)
- The Right window contains the chatbox where SayIt Assistant will display the user's prompt and SayIt Assistant's response
- The footer will has a "Start" button and a "Stop Recording " button will appear after "Start" is pressed

### Voice Commands
- SayIt Assistant is controlled via voice commands, the first few words in a prompt deciding what command is executed
    - Available commands are: "Question", "Delete", "Clear", and email commands described below

    - Examples:
        > Question, when did the War of 1812 end

        > Delete selected prompt

        > Clear all

### Email Functionality
- SayIt Assistant also can send emails, to do so setup the user's email using the command "Setup email"
    - A window will pop up with fields asking for: first name, last name, display name, email address, SMTP host, TLS port, and email password
        - At the footer, there are 2 buttons, "Save" and "Cancel"
            - Saving will save the information for use, Cancel will discard the information
    - After setup using the command "Create Email" will composed an email as desired
        - Format: "Create Email to [name], [email body]
        - Automatially adds send off at the bottom of the email
        - Displays prompt and email in the chatbox
    - After creating an email send it using the "Send Emai" command
        - Format: "Send Email to [name] at [email adress]

        - If successful, chatbox will display "Email Sucessfully Sent"
        - If not, chatbox will display the error