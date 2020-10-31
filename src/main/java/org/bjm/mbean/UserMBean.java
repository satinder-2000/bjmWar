/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjm.mbean;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.bjm.ejb.ForumBeanLocal;
import org.bjm.ejb.ReferenceDataBeanLocal;
import org.bjm.ejb.SurveyBeanLocal;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.Forum;
import org.bjm.model.LokSabha;
import org.bjm.model.State;
import org.bjm.model.Survey;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ConvertPngToJpg;
import org.bjm.util.ImageUtil;
import org.bjm.util.ImageVO;

/**
 *
 * @author root
 */
@Named(value = "userMBean")
@SessionScoped
public class UserMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(UserMBean.class.getName());
    private static final String DEFAULT_STATE_CODE="NA";
    private static final String ACCOUNT_ACTIONS="Account Actions";
    private static final String MY_ACTIVITIES="My Activities";
    private static final String MY_FORUMS="My Forums";
    private static final String MY_SURVEYS="My Surveys";
    private static final String AMEND_DETAILS="Amend Details";
    private static final String CHANGE_PASSWORD="Change Password";
    private static final String ACTIVITY_REMINDER="Activity Reminder";
    private static int[] ACTIVITY_REMINDER_OPTIONS={-1,1,2,3,4};
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    @Inject
    private ForumBeanLocal forumBeanLocal;
    
    @Inject
    private SurveyBeanLocal surveyBeanLocal;
    
    @Inject
    private ReferenceDataBeanLocal referenceDataBeanLocal;
    
    private List<State> states; 
    
    private User user;
    private Part profileFile;
    private String newEmail;
    private String orgStateCode;
    private String profileFileName;
    private String activity;
    private List<String> activities;
    private String listMode;
    
    private String accountAction;
    private List<String> accountActions;
    private String actionMode;
    private List<LokSabha> constituencies;
    private String userConstituencyId;
    private static final String SELECT_ONE="Select One";
            
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        user=(User)session.getAttribute(BJMConstants.USER);
        LokSabha ls=user.getConstituency();
        if (ls==null){
            ls=new LokSabha();
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
            ls.setConstituency(rb.getString("selectone"));
            ls.setId(0);
            user.setConstituency(ls);
        }
        newEmail=user.getEmail();
        orgStateCode=user.getStateCode();
        profileFileName=user.getProfileFile();
        states=new ArrayList<>();
        states.addAll(referenceDataBeanLocal.getStates());
        constituencies=new ArrayList();
        constituencies.addAll(referenceDataBeanLocal.getLokSabhasForState(user.getStateCode()));
        activities=new ArrayList<>();
        activities.add(MY_ACTIVITIES);
        activities.add(MY_FORUMS);
        activities.add(MY_SURVEYS);
        accountActions=new ArrayList<>();
        accountActions.add(ACCOUNT_ACTIONS);
        accountActions.add(AMEND_DETAILS);
        accountActions.add(CHANGE_PASSWORD);
        accountActions.add(ACTIVITY_REMINDER);
        LOGGER.info("User Loaded ID - "+user.getId());
        
    }
    
    public void activityListener(AjaxBehaviorEvent event){
        actionMode=null;
        UIOutput source=(UIOutput) event.getSource();
        String value=(String)source.getValue();
        if (value.equals(MY_FORUMS)){
            listMode=MY_FORUMS;
        }else if (value.equals(MY_SURVEYS)){
            listMode=MY_SURVEYS;
        }        
        LOGGER.log(Level.INFO, "Value is {0}", value);
    }
    
    public void accountActionListener(AjaxBehaviorEvent event){
        listMode=null;
        UIOutput source=(UIOutput) event.getSource();
        String value=(String)source.getValue();
        if (value.equals(AMEND_DETAILS)){
            actionMode=AMEND_DETAILS;
        }else if (value.equals(CHANGE_PASSWORD)){
            actionMode=CHANGE_PASSWORD;
        }else if (value.equals(ACTIVITY_REMINDER)){
            actionMode=ACTIVITY_REMINDER;
        }       
        LOGGER.log(Level.INFO, "Value is {0}", value);
    }
    
    public String updateFSReminder(){
        int value=user.getFsReminder();
        LOGGER.log(Level.INFO, "reminderActionListener value is :{0}", value);
        user=userBeanLocal.updateUserFSReminder(user);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("changesApplied"), rb.getString("changesApplied")));
        return null;
    }
    
    public List<Forum> getForumsByUser(){
        List<Forum> forums=forumBeanLocal.getForumsByUser(user.getId());
        LOGGER.log(Level.INFO, "Forums by User[{0}] extracted. Size is : {1}", new Object[]{user.getId(), forums.size()});
        return forums;
    }
    
    public List<Survey> getSurveysByUser(){
        List<Survey> surveys=surveyBeanLocal.getSurveysByUser(user.getId());
        LOGGER.info("Surveys by User["+user.getId()+"] extracted. Size is : "+surveys.size());
        return surveys;
    }
    
    public String amendUserDetails(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Firstname
        if(user.getFirstname().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("firstname", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("firstnameRequired"), rb.getString("firstnameRequired"))); 
        }else if(user.getFirstname().length()<2 || user.getFirstname().length()>45){
            FacesContext.getCurrentInstance().addMessage("firstname", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("firstNameCharsLimit"), rb.getString("firstNameCharsLimit")));
        }
        //Lastname
        if(user.getLastname().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("lastname", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("lastnameRequired"), rb.getString("lastnameRequired"))); 
        }else if(user.getLastname().length()<2 || user.getLastname().length()>45){
            FacesContext.getCurrentInstance().addMessage("lastname", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("lastNameCharsLimit"), rb.getString("lastNameCharsLimit")));
        }
        
        //Validate email if Exists
        if (newEmail.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailRequired"), rb.getString("emailRequired")));
        } else {//Email Regex validation
            if (!newEmail.equals(user.getEmail())) {
                Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
                Matcher m = p.matcher(newEmail);
                if (!m.find()) {
                    FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailInvalid"), rb.getString("emailInvalid")));
                } else {
                    boolean isEmailRegistered = userBeanLocal.isEmailRegistered(user.getEmail());
                    if (isEmailRegistered) {
                        FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailTaken"), rb.getString("emailTaken")));
                    } else {
                        user.setEmail(newEmail);
                    }
                }
            }
        }
        //Gender
        if (user.getGender().equals("")){
           FacesContext.getCurrentInstance().addMessage("gender", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("genderRequired"), rb.getString("genderRequired"))); 
        }
        
        //State and PostCode are mandatory
        String stateCode=user.getStateCode();
        if (!stateCode.equals(orgStateCode)) {
            if (stateCode.equals(DEFAULT_STATE_CODE)) {
                FacesContext.getCurrentInstance().addMessage("state", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("stateRequired"), rb.getString("stateRequired")));
            }
        }
        
        //LS Constituency
        if (user.getConstituency().getConstituency().equals(rb.getString("selectOne"))){
            FacesContext.getCurrentInstance().addMessage("userConstituency", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("noConstituency"), rb.getString("noConstituency")));
        }else{
            for(LokSabha ls: constituencies){
                if(user.getConstituency().getConstituency().equals(ls.getConstituency())){
                    user.setConstituency(ls);
                    break;
                }
            }
                
        }
                
            
        //Phone and Mobile
        String phone=user.getPhone();
        String mobile=user.getMobile();
        if (phone.isEmpty() && mobile.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("phone",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("phoneMobileRequired"), rb.getString("phoneMobileRequired")));
        }
        
        if (!phone.isEmpty()){
            if (!phone.startsWith("0")){//prefix
              FacesContext.getCurrentInstance().addMessage("phone",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("phoneSTDCode"), rb.getString("phoneSTDCode")));  
            }else{
                //Phone with STD Code (excluding the 0) must made 10 chars
                if (phone.length()!=11){
                  FacesContext.getCurrentInstance().addMessage("phone",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("totalDigitsPhone"), rb.getString("totalDigitsPhone")));  
                }
            }
        }
        
        
        if (!mobile.isEmpty()){
            if (!mobile.startsWith("0")){//prefix
              FacesContext.getCurrentInstance().addMessage("mobile",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("mobileSTDCode"), rb.getString("mobileSTDCode")));  
            }else{
                if (mobile.length()!=11){
                  FacesContext.getCurrentInstance().addMessage("mobile",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("totalDigitsMobile"), rb.getString("totalDigitsMobile")));  
                }
            }
            
        }
            
        //So far so good. Profile file now
        //Profile File size and compression
        saveProfileImage();
        
        String toReturn=null;
        List<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessageList();
        if (msgs != null && msgs.size() > 0) {
            toReturn = null;
        } else {
            user=userBeanLocal.amendUser(user);
            HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session=request.getSession();
            session.setAttribute(BJMConstants.USER, user);
            FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("changesApplied"), rb.getString("changesApplied")));
            toReturn = null;
        }
        return toReturn;
        
    }
    
    public String changePassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        String password=user.getPassword();
        String passwordConfirm=user.getPasswordConfirm();
        if (password.trim().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("pwd1",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("noPassword"),rb.getString("noPassword")));
        }else{
            //First, RegEx the password
            Pattern pCdIn=Pattern.compile(BJMConstants.PW_REGEX);
            Matcher mPCdIn=pCdIn.matcher(password);
            if (!mPCdIn.find()){
                FacesContext.getCurrentInstance().addMessage("pwd1",new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("invalidPassword"),rb.getString("invalidPassword")));  
            }else{//compare the password now
                if(!password.equals(passwordConfirm)){
                    FacesContext.getCurrentInstance().addMessage("pwd2",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,rb.getString("passwordsMisMatch"),rb.getString("passwordsMisMatch")));
                }
                
            }
        }
        List<FacesMessage> msgs= FacesContext.getCurrentInstance().getMessageList();
        if (msgs!=null && msgs.size()>0){
            return null;
        }else{
            user=userBeanLocal.changePassword(user);
            FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("changesApplied"), rb.getString("changesApplied")));
            return null;
        }
    }
    
    private void saveProfileImage() {
        
        BufferedImage logoBufferedImage=null;
        if (profileFile!=null && !profileFile.getSubmittedFileName().equals(profileFileName) ){//User changed the file to update
            try {
                InputStream input = profileFile.getInputStream();

                int fileSize = (int) profileFile.getSize();
                if (fileSize > (1000 * 1024)) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
                    FacesContext.getCurrentInstance().addMessage("profileFile",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("profileFileImgSize"), rb.getString("profileFileImgSize")));

                } else {//process with the processing of the image.
                    //Step 1 Resize the image
                    logoBufferedImage = ImageUtil.resizeImage(input, 150);
                    String fullFileName = profileFile.getSubmittedFileName();
                    String fileType = fullFileName.substring(fullFileName.indexOf('.'));
                    byte[] jpgData = null;
                    if (fileType.equals("png")) {//convert to jpg first. Jelastic' OpenJDK doen not handle png images well and throw exception.
                        byte[] pngData = new byte[input.available()];
                        jpgData = ConvertPngToJpg.convertToJpg(pngData);
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(logoBufferedImage, "jpg", baos);
                        baos.flush();
                        jpgData = baos.toByteArray();
                        baos.close();
                    }
                    user.setProfileFile(fullFileName);
                    profileFileName=user.getProfileFile();
                    user.setImage(jpgData);

                }
            } catch (IOException ex) {
                Logger.getLogger(UserRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
            
        //We would need to display the Image in the ConfirmPage, which is next in the Flow.
        //There is no solution for that - only a workaround.
        //We put this image in the session for now and once the Deeder data has been persisted in the Database the image from the session will be removed.
        /*HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(true);
        String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
        ImageVO imageVO=new ImageVO(imgType,user.getImage());
        session.setAttribute(BJMConstants.TEMP_IMAGE, imageVO);*/
        //This Image will be removed from Session once the data has been persisted.
    }
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getOrgStateCode() {
        return orgStateCode;
    }

    public void setOrgStateCode(String orgStateCode) {
        this.orgStateCode = orgStateCode;
    }
    
    
    

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public Part getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(Part profileFile) {
        this.profileFile = profileFile;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getListMode() {
        return listMode;
    }

    public void setListMode(String listMode) {
        this.listMode = listMode;
    }

    public String getAccountAction() {
        return accountAction;
    }

    public void setAccountAction(String accountAction) {
        this.accountAction = accountAction;
    }

    public List<String> getAccountActions() {
        return accountActions;
    }

    public void setAccountActions(List<String> accountActions) {
        this.accountActions = accountActions;
    }

    public String getActionMode() {
        return actionMode;
    }

    public void setActionMode(String actionMode) {
        this.actionMode = actionMode;
    }

    public static int[] getACTIVITY_REMINDER_OPTIONS() {
        return ACTIVITY_REMINDER_OPTIONS;
    }

    public static void setACTIVITY_REMINDER_OPTIONS(int[] ACTIVITY_REMINDER_OPTIONS) {
        UserMBean.ACTIVITY_REMINDER_OPTIONS = ACTIVITY_REMINDER_OPTIONS;
    }

    public List<LokSabha> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<LokSabha> constituencies) {
        this.constituencies = constituencies;
    }

    public String getUserConstituencyId() {
        return userConstituencyId;
    }

    public void setUserConstituencyId(String userConstituencyId) {
        this.userConstituencyId = userConstituencyId;
    }

    
    
    
    
    
    
    
    
}
