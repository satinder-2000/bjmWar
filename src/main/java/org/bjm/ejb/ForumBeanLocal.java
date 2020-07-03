/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.ejb;

import java.util.List;
import javax.ejb.Local;
import org.bjm.model.Forum;
import org.bjm.model.ForumAbuse;
import org.bjm.model.ForumComment;
import org.bjm.model.User;

/**
 *
 * @author root
 */
@Local
public interface ForumBeanLocal {
    
    public Forum createForum(Forum forum, User user );

    public List<Forum> getForums();
    
    public List<Forum> getForumsByUser(int userId);

    public Forum getForumById(int forumId);

    public Forum addForumComment(Forum forum, ForumComment fc);

    public List<ForumComment> getForumComments(int id);
    
    public ForumComment getForumCommentById(int commentId);
    
    public ForumAbuse addForumAbuse(ForumAbuse forumAbuse);
    
}
