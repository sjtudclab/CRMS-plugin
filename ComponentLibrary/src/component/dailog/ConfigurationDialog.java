package component.dailog;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;

import component.requestSender.HttpSender;
import component.writer.StringHandler;

public class ConfigurationDialog extends Dialog {

//	private Text localAddressText;
	private Text usernameText;
	private Text passwordText;
//	private Text RepositoryNameText;
//	private Text BranchNameText;
	
	//	private MessageDialog m = null;
	
	public ConfigurationDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	protected int getShellStyle() {
		return super.getShellStyle();// | SWT.RESIZE | SWT.MAX;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
    }
	
	protected Point getInitialSize(){
        return new Point(450,250);
    }
	
	protected Control createDialogArea(Composite parent){
		getShell().setText("Configuration");
		
		Label titleHint = new Label(parent,SWT.NONE);
        titleHint.setText("Set the configuration.");
        Font font = new Font(parent.getDisplay(), "Î¢ÈíÑÅºÚ", 10, SWT.BOLD);      
        titleHint.setFont(font);
		
        final Label titleMsg = new Label(parent,SWT.NONE);
        titleMsg.setText("Setting the infomation.");
        titleMsg.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        GridLayout layoutWhole = new GridLayout();
		layoutWhole.marginHeight = 0;
		layoutWhole.marginWidth = 30;
		layoutWhole.marginBottom = 0;
		layoutWhole.marginTop = 20;
		parent.setLayout(layoutWhole);
       
//		final Group groupLocal = new Group(parent,SWT.None);
//		groupLocal.setBounds(0, 0, 0, 0);
//		groupLocal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		groupLocal.setText("Local Repository");
		
//		GridLayout layoutUser = new GridLayout();
//		layoutUser.marginHeight = 0;
//		layoutUser.marginWidth = 30;
//		layoutUser.marginBottom = 0;
//		layoutUser.marginTop = 20;
//		parent.setLayout(layoutUser);
//		
		Group groupUser = new Group(parent,SWT.None);
		groupUser.setBounds(0, 0, 0, 0);
		groupUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupUser.setText("Configuration");
//		
//		GridLayout layoutUserContainer = new GridLayout();
//		layoutUserContainer.numColumns = 2;
//		groupUser.setLayout(layoutUserContainer);
//		
//		GridLayout layoutRemote = new GridLayout();
//		layoutRemote.marginHeight = 0;
//		layoutRemote.marginWidth = 30;
//		layoutRemote.marginBottom = 0;
//		layoutRemote.marginTop = 20;
//		parent.setLayout(layoutRemote);
//		
//		Group groupRemote = new Group(parent,SWT.None);
//		groupRemote.setBounds(0, 0, 0, 0);
//		groupRemote.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		groupRemote.setText("Remote Repository");
//
//		GridLayout layoutRemoteContainer = new GridLayout();
//		layoutRemoteContainer.numColumns = 2;
//		groupRemote.setLayout(layoutRemoteContainer);
		
		//for GroupLocal
//		GridLayout layoutCompositeLocal = new GridLayout();
//		layoutCompositeLocal.numColumns = 3;
//		layoutCompositeLocal.marginWidth = 10;
//		layoutCompositeLocal.marginHeight = 10;
//		groupLocal.setLayout(layoutCompositeLocal);
//        
//        GridData gd_L = new GridData(SWT.FILL,SWT.FILL,true,true,2,2);
//        gd_L.heightHint = 200;
//        groupUser.setLayoutData(gd_L);

//        Label localAddressLabel = new Label(groupLocal,SWT.NONE);
//        localAddressLabel.setText("Local Address:");           
//        localAddressText = new Text(groupLocal,SWT.BORDER);
//        localAddressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//        Button localBtn = new Button(groupLocal, SWT.PUSH);
//        localBtn.setBounds(0, 0, 200, 30);
//        localBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
//        localBtn.setText(" ... "); 
		
//        localBtn.addSelectionListener(new SelectionAdapter() {
//        	public void widgetSelected(SelectionEvent evt){
//        		DirectoryDialog d = new DirectoryDialog(groupLocal.getShell());  
//        		d.setMessage("Select a Local Address as the Repository");  
//        		d.setText("Local Repository");  
//        		d.setFilterPath("D://");
//        		localAddressText.setText(d.open());
//        	}
//        });
        
		//for GroupUser
		GridLayout layoutComposite = new GridLayout();
        layoutComposite.numColumns = 2;
        layoutComposite.marginWidth = 10;
        layoutComposite.marginHeight = 10;
        //layoutComposite.marginBottom = 10;
        groupUser.setLayout(layoutComposite);
        
        GridData gd_g = new GridData(SWT.FILL,SWT.FILL,true,true,2,2);
        gd_g.heightHint = 200;
        groupUser.setLayoutData(gd_g);

        Label usernameLabel = new Label(groupUser,SWT.NONE);
        usernameLabel.setText("username:");           
        usernameText = new Text(groupUser,SWT.BORDER);
        usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label passwordLabel = new Label(groupUser,SWT.NONE);
        passwordLabel.setText("password:");           
        passwordText = new Text(groupUser,SWT.BORDER);
        passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
           		
//		//for GroupRemote
//        GridLayout layoutComposite2 = new GridLayout();
//        layoutComposite2.numColumns = 2;
//        layoutComposite2.marginWidth = 10;
//        layoutComposite2.marginHeight = 10;
//        //layoutRemote.marginBottom = 10;
//        groupRemote.setLayout(layoutComposite2);
//        
//        GridData gd_R = new GridData(SWT.FILL,SWT.FILL,true,true,2,2);
//        gd_R.heightHint = 200;
//        groupRemote.setLayoutData(gd_R);
//
//        Label RepositoryNameLabel = new Label(groupRemote,SWT.NONE);
//        RepositoryNameLabel.setText("Repository Name:");           
//        RepositoryNameText = new Text(groupRemote,SWT.BORDER);
//        RepositoryNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//        Label BranchNameLabel = new Label(groupRemote,SWT.NONE);
//        BranchNameLabel.setText("Branch:");           
//        BranchNameText = new Text(groupRemote,SWT.BORDER);
//        BranchNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//for Button
        GridLayout layoutButton = new GridLayout();
        layoutButton.marginHeight = 0;
        layoutButton.marginWidth = 10;
		layoutButton.numColumns = 2;
		layoutButton.marginRight = 0;
		layoutButton.marginLeft = 230;
        
        Composite composite = new Composite(parent,SWT.NONE);
		composite.setLayout(layoutButton);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button cancelBtn = new Button(composite, SWT.PUSH);
        cancelBtn.setBounds(0, 0, 200, 30);
        cancelBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        cancelBtn.setText(" cancel ");
        
        Button confirmBtn = new Button(composite, SWT.PUSH);
        confirmBtn.setBounds(0, 0, 200, 30);
        confirmBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        confirmBtn.setText("confirm");
        
        confirmBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
        		if(saveState()){
        			if(login()){
        				//buildReposity(localAddressText.getText());
        				close();
        			}else{
        				System.out.println("test");
        				titleMsg.setText("Wrong Username or Password");
        				//titleHint2.setFont(font2);
        			}
        		}
        	}
        });
        
        cancelBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){    
        		close();
        	}
        });
               
        if (usernameText.getText() == null || usernameText.getText().equals("")){
        	restoreState();
        }
        
        return parent;
	}

	public boolean saveState(){
		StringHandler handler = new StringHandler();
		if (usernameText.getText() == null || usernameText.getText().equals("")
		 || passwordText.getText() == null || passwordText.getText().equals("")){
			handler.StringDelete("username");
			handler.StringDelete("password");
			handler.StringDelete("id");
			handler.StringDelete("private_token");
			return false;
		}else{
			//System.out.println(localAddressText.getText());
//			handler.StringWrite("localAddress", localAddressText.getText());
			handler.StringWrite("username", usernameText.getText());
			handler.StringWrite("password", passwordText.getText());
//			handler.StringWrite("remoteRepository", RepositoryNameText.getText());
//			handler.StringWrite("branch", BranchNameText.getText());
			
			return true;
		}
	}

	public void restoreState(){
		if (usernameText.getText() == null || usernameText.getText().equals("")
		 || passwordText.getText() == null || passwordText.getText().equals("")){
			StringHandler handler = new StringHandler();
			String tmp;
//			tmp = handler.StringRead("localAddress");
//			if(tmp == null)
//				tmp = "";
//			localAddressText.setText(tmp);
			
			tmp = handler.StringRead("username");
			if(tmp == null)
				tmp = "";
			usernameText.setText(tmp);
			
			tmp = handler.StringRead("password");
			if(tmp == null)
				tmp = "";
			passwordText.setText(tmp);
			
//			tmp = handler.StringRead("remoteRepository");
//			if(tmp == null)
//				tmp = "";
//			RepositoryNameText.setText(tmp);
//			
//			tmp = handler.StringRead("branch");
//			if(tmp == null)
//				tmp = "";
//			BranchNameText.setText(tmp);
		}
    }
	
	private boolean login() {
		StringHandler handler = new StringHandler();
		String username = handler.StringRead("username");
		String password = handler.StringRead("password");
		
		HttpSender sender = new HttpSender();
		JSONArray result = sender.getRequest("/user/login", "username=" + username + "&password=" + password);
		if(result != null){
			try {
				handler.StringWrite("private_token", result.getJSONObject(0).getString("private_token"));
				handler.StringWrite("id", result.getJSONObject(0).getString("id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else
			handler.StringDelete("private_token");
			return false;
	}

}
