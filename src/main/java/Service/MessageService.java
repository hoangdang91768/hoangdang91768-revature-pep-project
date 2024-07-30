package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.findMessageById(messageId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.findAllMessages();
    }

    public List<Message> getAllMessagesByUserId(int userId) {
        return messageDAO.findAllMessagesByUserId(userId);
    }

    public Message deleteMessageById(int messageId) {
        Message message = messageDAO.findMessageById(messageId);
        if (message == null) {
            return null;
        }
        boolean deleted = messageDAO.deleteMessageById(messageId);
        if (deleted) {
            return message;
        } else {
            return null;
        }
    }
    

    public Message updateMessageTextByIdAndReturnMessage(int messageId, String newMessageText) {
        // Validate message text
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null;  // Indicate invalid message text
        }
        
        try {
            // Find the existing message
            Message message = messageDAO.findMessageById(messageId);
            if (message == null) {
                return null;  // Indicate message not found
            }
            
            // Update the message
            boolean updated = messageDAO.updateMessageTextById(messageId, newMessageText);
            if (updated) {
                message.setMessage_text(newMessageText);
                return message;  // Return the updated message
            } else {
                return null;  // Indicate update failure
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Indicate error
        }
    }    
    
}
