package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserId);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageTextById);

        return app;
    }

    private void registerAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account createdAccount = accountService.registerAccount(account);
        if (createdAccount != null) {
            ctx.status(200).json(createdAccount);
        } else {
            ctx.status(400);
        }
    }

    private void login(Context ctx) {
        Account loginAttempt = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(loginAttempt.getUsername(), loginAttempt.getPassword());
        if (loggedInAccount != null) {
            ctx.status(200).json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200).json("");
        }
    }

    private void getAllMessagesByUserId(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUserId(userId);
        ctx.status(200).json(messages);
    }

    private void deleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if (deletedMessage != null) {
            ctx.status(200).json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageTextById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper objectMapper = new ObjectMapper();
        String newMessageText;
        
        try {
            newMessageText = objectMapper.readTree(ctx.body()).get("message_text").asText();
        } catch (Exception e) {
            ctx.status(400).result("Invalid JSON format");
            return;
        }
        
        // check if message text is invalid (empty or too long)
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            ctx.status(400).result("");  // bad request
            return;
        }
        
        try {
            Message updatedMessage = messageService.updateMessageTextByIdAndReturnMessage(messageId, newMessageText);
            
            if (updatedMessage != null) {
                ctx.status(200).json(updatedMessage);
            } else {
                ctx.status(400).result("");  // message not found or invalid input
            }
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
    
    
}