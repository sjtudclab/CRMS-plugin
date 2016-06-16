package component.dailog;

import java.util.Map;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;

import component.git.JgitHelper;
import component.requestSender.HttpSender;
import component.writer.StringHandler;

public class PushDialog extends Dialog {
	private Table table;
	private Label localAddressText;
	private Label remoteAddressText;
	private Text MessageText;
	private Button pushBtn;
	private Button enterBtn;
	
	private JSONArray projectArray;
	private StringHandler handler = new StringHandler();
	private JgitHelper jgitHelper = new JgitHelper();
	
	public PushDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	protected int getShellStyle() {
		return super.getShellStyle();// | SWT.RESIZE | SWT.MAX;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
    }
	
	protected Point getInitialSize(){
        return new Point(550,500);
    }
	
	protected Control createDialogArea(Composite parent){
		getShell().setText("Download");

        Label titleHint = new Label(parent,SWT.NONE);
        titleHint.setText("Push the project.");
        Font font = new Font(parent.getDisplay(), "微软雅黑", 10, SWT.BOLD);      
        titleHint.setFont(font);
        
		final Group groupLocal = new Group(parent,SWT.None);
		groupLocal.setBounds(0, 0, 0, 0);
		groupLocal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupLocal.setText("Local Repository");
		
		GridLayout layoutWhole = new GridLayout();
		layoutWhole.marginHeight = 10;
		layoutWhole.marginWidth = 10;
		layoutWhole.marginBottom = 0;
		layoutWhole.marginTop = 0;
		parent.setLayout(layoutWhole);
		
		GridLayout layoutCompositeLocal = new GridLayout();
		layoutCompositeLocal.numColumns = 2;
		layoutCompositeLocal.marginWidth = 10;
		layoutCompositeLocal.marginHeight = 10;
		groupLocal.setLayout(layoutCompositeLocal);
        
        Label localAddressLabel = new Label(groupLocal,SWT.NONE);
        localAddressLabel.setText("Local Address:");           
        localAddressText = new Label(groupLocal,SWT.NONE);
        localAddressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localAddressText.setText(handler.StringRead("localAddress"));
        
        Label remoteAddressLabel = new Label(groupLocal,SWT.NONE);
        remoteAddressLabel.setText("Remote Repository:");           
        remoteAddressText = new Label(groupLocal,SWT.NONE);
		jgitHelper.setFilePath(handler.StringRead("localAddress"));
		remoteAddressText.setText(jgitHelper.getUri());
		
		Label MessageLabel = new Label(groupLocal,SWT.NONE);
		MessageLabel.setText("commit Message:");         
		MessageText = new Text(groupLocal,SWT.BORDER);
		MessageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group tableGroup = new Group(parent,SWT.None);
		tableGroup.setBounds(0, 20, 200, 200);
		tableGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableGroup.setText("File List");
		
		GridData gd_tb = new GridData(GridData.FILL_BOTH);
		gd_tb.heightHint = 20;
		tableGroup.setLayoutData(gd_tb);
		tableGroup.setLayout(new GridLayout(1,false));
		
		table = new Table(tableGroup,SWT.MULTI | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        
     // 创建表头的字符串数组  
        projectArray = getContent();
     //   if(projectArray == null) return parent;	//没有获取到东西
        
        String[] tableHeader = {"Component Name", "Description", "branch", "state"};
        int[] tableWidth = {150,170,95,90};
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
            tableColumn.setText(tableHeader[i]);
            tableColumn.setMoveable(true);  
            tableColumn.setWidth(tableWidth[i]);
        }
        
        for(int i = 0;i < projectArray.length();i++){
            try {
            	JSONArray branchArray = getBranch(projectArray.getJSONObject(i).getString("id"));
            	for(int j = 0;j < branchArray.length();j++){
                	TableItem item = new TableItem(table, SWT.NONE);
                	String s = "Developing";
            		item.setText(new String[]{
            				projectArray.getJSONObject(i).getString("name"),
            				projectArray.getJSONObject(i).getString("description"),
            				branchArray.getJSONObject(j).getString("name"),
            				s});
            	}
            } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }
        
        GridLayout layoutButton = new GridLayout();
        layoutButton.marginHeight = 0;
        layoutButton.marginWidth = 10;
		layoutButton.numColumns = 3;
		layoutButton.marginRight = 0;
		layoutButton.marginLeft = 280;
		
        Composite composite = new Composite(parent,SWT.NONE);
		composite.setLayout(layoutButton);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button cancelBtn = new Button(composite, SWT.PUSH);
        cancelBtn.setBounds(0, 0, 200, 30);
        cancelBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        cancelBtn.setText(" cancel ");
        
        pushBtn = new Button(composite, SWT.PUSH);
        pushBtn.setBounds(0, 0, 200, 30);
        pushBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        pushBtn.setText("confirm");
        
        enterBtn = new Button(composite, SWT.PUSH);
        enterBtn.setBounds(0, 0, 200, 60);
        enterBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        enterBtn.setText("enter repository");
        
        pushBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
        		//System.out.println(table.getSelectionIndex());\
        		if(push());        		
        			close();
        	}
        });
        
        enterBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
        		if(enter());
        			close();
        	}
        });
        
        cancelBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){    
        		close();
        	}
        });
        
        // 为单元格注册选中事件  
        table.addSelectionListener(new SelectionAdapter()  
        {  
            public void widgetSelected(SelectionEvent e)  
            {  
                // 获得所有的行数  
                int total = table.getItemCount();  
                // 循环所有行  
                for (int i = 0; i < total; i++)  
                {  
                    TableItem item = table.getItem(i);  
                    // 如果该行为选中状态，改变背景色和前景色，否则颜色设置  
                    if (table.isSelected(i))  
                    {  
                        item.setBackground(getShell().getDisplay().getSystemColor(  
                                SWT.COLOR_RED));  
                        item.setForeground(getShell().getDisplay().getSystemColor(  
                                SWT.COLOR_DARK_GREEN));
                        try{
	                        remoteAddressText.setText(projectArray.getJSONObject(table.getSelectionIndex()).getString("git_url"));
	                        remoteAddressText.setSize(400, 20);
	                        
	                		jgitHelper.setFilePath(localAddressText.getText());
	                		if(!jgitHelper.getUri().equals(projectArray.getJSONObject(table.getSelectionIndex()).getString("git_url")) && jgitHelper.getUri() != ""){
	                			//System.out.println(projectArray.getJSONObject(table.getSelectionIndex()).getString("git_url"));
	                			//System.out.println(jgitHelper.getUri());
	                			pushBtn.setEnabled(false);
	                			enterBtn.setEnabled(false);
	                		}else{
	                			pushBtn.setEnabled(true);
	                			enterBtn.setEnabled(true);
	                		}
                		}catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }  
                    else  
                    {  
                        item.setBackground(null);  
                        item.setForeground(null);  
                    }  
                }  
            }  
  
        });
        
		pushBtn.setEnabled(false);
		enterBtn.setEnabled(false);
        
		return parent; 

	}

	private JSONArray getContent(){
		HttpSender sender = new HttpSender();
		StringHandler handler = new StringHandler();
		String token = handler.StringRead("private_token");
		
		JSONArray result = sender.getRequest("/group/allProject", "private_token=" + token);
		
		for(int i = 0; i < result.length();i++){
			try {
				int pid = result.getJSONObject(i).getInt("id");
				System.out.println(pid);
				JSONArray result_detail = sender.getRequest("/project/getDetail", "private_token=" + token + "&id=" + pid);			
				result.getJSONObject(i).put("git_url",result_detail.getJSONObject(0).getString("http_url_to_repo"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	private JSONArray getBranch(String id){
		HttpSender sender = new HttpSender();
		
		StringHandler handler = new StringHandler();
		String token = handler.StringRead("private_token");
		
		JSONArray result = sender.getRequest("/project/branch", "private_token=" + token + "&id=" + id);
		return result;
	}
	private Boolean push(){
		if(MessageText.getText().equals(""))
			return false;
		jgitHelper.gitPush(MessageText.getText());
		return true;
	}
	private Boolean enter(){
		if(MessageText.getText().equals(""))
			return false;		
		String uid = handler.StringRead("id");
		String pid = null;
		
		try {
			pid = projectArray.getJSONObject(table.getSelectionIndex()).getJSONObject("namespace").getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String descrpition = MessageText.getText();
		
		HttpSender sender = new HttpSender();
		sender.getRequest("/order/save", "uid=" + uid + "&pid=" + pid + "&description=" + descrpition);
		//push();
		return true;
 	}
}
