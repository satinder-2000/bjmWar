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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.flow.FlowScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.bjm.ejb.ReferenceDataBeanLocal;
import org.bjm.ejb.UserBeanLocal;
import org.bjm.model.LokSabha;
import org.bjm.model.State;
import org.bjm.model.User;
import org.bjm.util.BJMConstants;
import org.bjm.util.ConvertPngToJpg;
import org.bjm.util.ImageUtil;
import org.bjm.util.ImageVO;

/**
 *
 * @author root
 */
@Named(value = "userRegisterMBean")
@FlowScoped("UserRegister")
public class UserRegisterMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(UserRegisterMBean.class.getName());
    private static final String DEFAULT_STATE_CODE="NA";
    
    private ExternalContext externalContext ;
    
    @Inject
    private UserBeanLocal userBeanLocal;
    
    @Inject
    private ReferenceDataBeanLocal referenceDataBeanLocal;
    
    @ManagedProperty("#{msg}")
    private ResourceBundle bundle;
    
    private List<State> states;
    private List<LokSabha> constituencies;
    private String userConstituencyId;
    private static final String SELECT_ONE="Select One";
    private User user;
    private State userState;
    private Part profileFile; 
    private boolean acceptedTC;
    
    
    @PostConstruct
    public void init(){
        externalContext=FacesContext.getCurrentInstance().getExternalContext();
        user=new User();
        states=new ArrayList<>();
        State dummy=new State();
        dummy.setCode(DEFAULT_STATE_CODE);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        dummy.setName(rb.getString("pleaseSelectOne"));
        states.add(dummy);
        states.addAll(referenceDataBeanLocal.getStates());
        LOGGER.info("New User initialised");
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.log(Level.INFO, "State Code is {0}", user.getStateCode());
        constituencies=new ArrayList();
        LokSabha dummy=new LokSabha();
        dummy.setId(0);
        dummy.setConstituency(SELECT_ONE);
        constituencies.add(dummy);
        constituencies.addAll(referenceDataBeanLocal.getLokSabhasForState(user.getStateCode()));
        LOGGER.log(Level.INFO, "Total Constituencies incl. Dummy is : {0}", constituencies.size());
    }
    
    public String processData(){
        String toReturn = validateUser();
        if (toReturn != null) {
            toReturn = "UserConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public String amendDetails(){
        return "UserRegister?faces-redirect=true";
    }
    
    
    public String validateUser(){
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
        if (user.getEmail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailRequired"), rb.getString("emailRequired")));
        } else {//Email Regex validation
            Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
            Matcher m = p.matcher(user.getEmail());
            if (!m.find()) {
                FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailInvalid"), rb.getString("emailInvalid")));
            } else {
                boolean isEmailRegistered = userBeanLocal.isEmailRegistered(user.getEmail());
                if (isEmailRegistered) {
                    FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailTaken"), rb.getString("emailTaken")));
                } 
            }

        }
        
        //DOB > 16
        if (user.getDobStr().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("dob",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("dobRequired"), rb.getString("dobRequired")));
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            user.setDob(LocalDate.parse(user.getDobStr(), formatter));
            LocalDate dobUser = user.getDob();
            LocalDate dateToday = LocalDate.now();
            Period age = Period.between(dobUser, dateToday);
            if (age.getYears() < 16) {
                FacesContext.getCurrentInstance().addMessage("dob", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("minAge"), rb.getString("minAge")));
           
        }
        }
        
        
        //Gender
        if (user.getGender()==null){
           FacesContext.getCurrentInstance().addMessage("gender", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("genderRequired"), rb.getString("genderRequired"))); 
        }
        
        //State, Constituency and PostCode are mandatory
        String stateCode=user.getStateCode();
        if (stateCode.equals(DEFAULT_STATE_CODE)){
             FacesContext.getCurrentInstance().addMessage("state",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("stateRequired"), rb.getString("stateRequired")));  
        }else{
            for (State s:states){
                if (s.getCode().equals(user.getStateCode())){
                    userState=s;
                    break;
                }
            }
        }
        
        //LS Constituency
        if (userConstituencyId.equals("0")){
            FacesContext.getCurrentInstance().addMessage("userConstituency", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("noConstituency"), rb.getString("noConstituency")));
        }else{
            int constiId=Integer.parseInt(userConstituencyId);
            for(LokSabha ls: constituencies){
                if (ls.getId()==constiId){
                    user.setConstituency(ls);
                    break;
                }
            }
                
        }
            
        //Phone and Mobile
        String phone=user.getPhone();
        String mobile=user.getMobile();
        Pattern p = Pattern.compile(BJMConstants.PHONE_REGEX);
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
                }else {
                    Matcher m = p.matcher(phone);
                    if (!m.find()){
                       FacesContext.getCurrentInstance().addMessage("phone",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("invalidPhone"), rb.getString("invalidPhone")));  
                    }
                }
            }
        }
        
        
        if (!mobile.isEmpty()){
            if (!mobile.startsWith("0")){//prefix
              FacesContext.getCurrentInstance().addMessage("mobile",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("mobileSTDCode"), rb.getString("mobileSTDCode")));  
            }else{
                if (mobile.length()!=11){
                  FacesContext.getCurrentInstance().addMessage("mobile",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("totalDigitsMobile"), rb.getString("totalDigitsMobile")));  
                }else {
                    Matcher m = p.matcher(phone);
                    if (!m.find()){
                       FacesContext.getCurrentInstance().addMessage("mobile",new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("invalidMobile"), rb.getString("invalidMobile")));  
                    }
                }
            }
            
        }
            
        String toReturn=null;
        List<FacesMessage> msgs = FacesContext.getCurrentInstance().getMessageList();
        if (msgs != null && msgs.size() > 0) {
            toReturn = null;
        } else {
            //So far so good. Profile file now
            //Profile File size and compression
            saveProfileImage();
            toReturn = "";
        }
        return toReturn;
    }
    
    private void saveProfileImage() {
        
        BufferedImage logoBufferedImage=null;
        if (profileFile==null){//User did not Upload the Profile file. Make Avatar with the Initials
            try {
                char[] chars = new char[2];
                String sizeStr = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("iconSize");
                String imgFormat = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("imgFormat");
                int size = Integer.parseInt(sizeStr);
                chars[0] = user.getFirstname().charAt(0);
                chars[1] = user.getLastname().charAt(0);
                String text = new String(chars);
                logoBufferedImage = ImageUtil.drawIcon(size, text);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(logoBufferedImage, imgFormat, baos);
                baos.flush();
                byte[] jpgData = baos.toByteArray();
                baos.close();
                user.setProfileFile(text+"."+imgFormat);
                user.setImage(jpgData);
            } catch (IOException ex) {
                Logger.getLogger(UserRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
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
                String fileType=fullFileName.substring(fullFileName.indexOf('.'));
                byte[] jpgData=null;
                if (fileType.equals("png")){//convert to jpg first. Jelastic' OpenJDK doen not handle png images well and throw exception.
                    byte[] pngData=new byte[input.available()];
                    jpgData=ConvertPngToJpg.convertToJpg(pngData);
                }else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(logoBufferedImage, "jpg", baos);
                    baos.flush();
                    jpgData = baos.toByteArray();
                    baos.close();
                }
                user.setProfileFile(fullFileName);
                user.setImage(jpgData);
                
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
            
        }
        //We would need to display the Image in the ConfirmPage, which is next in the Flow.
        //There is no solution for that - only a workaround.
        //We put this image in the session for now and once the Deeder data has been persisted in the Database the image from the session will be removed.
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(true);
        String imgType=user.getProfileFile().substring(user.getProfileFile().indexOf('.')+1);
        ImageVO imageVO=new ImageVO(imgType,user.getImage());
        session.setAttribute(BJMConstants.TEMP_IMAGE, imageVO);//This Image will be removed from Session once the data has been persisted.
    }
    
    public void submitDetails() {
        user = userBeanLocal.createUser(user);
        LOGGER.log(Level.INFO, "User persisted with ID: {0} ",user.getId());
        //User has been sussessfully persisted in the Database. Now the Image (byte[]) can be removed from the session as well. 
        HttpServletRequest request= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        session.removeAttribute(BJMConstants.TEMP_IMAGE);
        LOGGER.info("Temporary Image of User removed from the session.");
    }
    
    public String getReturnValue() {
        submitDetails();
        return "/flowreturns/UserRegister-return?faces-redirect=true";
    }

    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getUserState() {
        return userState;
    }

    public void setUserState(State userState) {
        this.userState = userState;
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

    public boolean isAcceptedTC() {
        return acceptedTC;
    }

    public void setAcceptedTC(boolean acceptedTC) {
        this.acceptedTC = acceptedTC;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
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
