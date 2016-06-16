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
import org.json.JSONObject;

import component.git.JgitHelper;
import component.requestSender.HttpSender;
import component.writer.StringHandler;

public class DownloadDialog extends Dialog {
	private Table table;
	private HttpSender sender;
	private StringHandler handler;
	private JgitHelper jgitHelper = new JgitHelper();
	
	private Text localAddressText;
	private Label remoteAddressText;
	private Button confirmBtn;
	
	public DownloadDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		
		sender = new HttpSender();
		handler = new StringHandler();
		//jgitHelper = new JgitHelper();
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
        titleHint.setText("Select the file to download.");
        Font font = new Font(parent.getDisplay(), "微软雅黑", 10, SWT.BOLD);      
        titleHint.setFont(font);
		
		GridLayout layoutWhole = new GridLayout();
		layoutWhole.marginHeight = 10;
		layoutWhole.marginWidth = 10;
		layoutWhole.marginBottom = 0;
		layoutWhole.marginTop = 0;
		parent.setLayout(layoutWhole);
		
		final Group groupLocal = new Group(parent,SWT.None);
		groupLocal.setBounds(0, 0, 0, 0);
		groupLocal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupLocal.setText("Local Repository");
		
		GridLayout layoutCompositeLocal = new GridLayout();
		layoutCompositeLocal.numColumns = 3;
		layoutCompositeLocal.marginWidth = 10;
		layoutCompositeLocal.marginHeight = 10;
		groupLocal.setLayout(layoutCompositeLocal);
        
        Label localAddressLabel = new Label(groupLocal,SWT.NONE);
        localAddressLabel.setText("Local Address:");           
        localAddressText = new Text(groupLocal,SWT.BORDER);
        localAddressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button localBtn = new Button(groupLocal, SWT.PUSH);
        localBtn.setBounds(0, 0, 200, 30);
        localBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        localBtn.setText(" ... "); 
		
        localBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
        		DirectoryDialog d = new DirectoryDialog(groupLocal.getShell());  
        		d.setMessage("Select a Local Address as the Repository");  
        		d.setText("Local Repository");  
        		d.setFilterPath("D://");
        		localAddressText.setText(d.open());
        	}
        });
		
        Label remoteAddressLabel = new Label(groupLocal,SWT.NONE);
        remoteAddressLabel.setText("Remote Repository:");           
        remoteAddressText = new Label(groupLocal,SWT.NONE);
        
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
        JSONArray projectArray = getContent();
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
        
        final JSONArray res = new JSONArray();
        
        for(int i = 0;i < projectArray.length();i++){
            try {
            	JSONArray branchArray = getBranch(projectArray.getJSONObject(i).getString("id"));
            	for(int j = 0;j < branchArray.length();j++){
            		JSONObject obj = new JSONObject();
            		obj.put("id", projectArray.getJSONObject(i).getInt("id"));
            		obj.put("branch", branchArray.getJSONObject(j).getString("name"));
            		obj.put("git_url", projectArray.getJSONObject(i).getString("git_url"));
            		res.put(obj);
            		
                	TableItem item = new TableItem(table, SWT.NONE);
                	String s = projectArray.getJSONObject(i).getString("completed").equals("1") ? "Completed" : "Developing";
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
    					try {
							remoteAddressText.setText(res.getJSONObject(table.getSelectionIndex()).getString("git_url"));
							
							jgitHelper.setFilePath(localAddressText.getText());
							if(!jgitHelper.getUri().equals(res.getJSONObject(table.getSelectionIndex()).getString("git_url")) && jgitHelper.getUri() != ""){
								System.out.println(res.getJSONObject(table.getSelectionIndex()).getString("git_url"));
								System.out.println(jgitHelper.getUri());
								confirmBtn.setEnabled(false);
							}else{
								confirmBtn.setEnabled(true);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
    					remoteAddressText.setSize(350, 20);
                    }  
                    else  
                    {  
                        item.setBackground(null);  
                        item.setForeground(null);  
                    }  
                }  
            }  
  
        });
        
        GridLayout layoutButton = new GridLayout();
        layoutButton.marginHeight = 0;
        layoutButton.marginWidth = 10;
		layoutButton.numColumns = 2;
		layoutButton.marginRight = 0;
		layoutButton.marginLeft = 380;
		
        Composite composite = new Composite(parent,SWT.NONE);
		composite.setLayout(layoutButton);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button cancelBtn = new Button(composite, SWT.PUSH);
        cancelBtn.setBounds(0, 0, 200, 30);
        cancelBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        cancelBtn.setText(" cancel ");
        
        confirmBtn = new Button(composite, SWT.PUSH);
        confirmBtn.setBounds(0, 0, 200, 30);
        confirmBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        confirmBtn.setText("confirm");
        
        confirmBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
				try {
					String uri = res.getJSONObject(table.getSelectionIndex()).getString("git_url");
					String local = localAddressText.getText();
					handler.StringWrite("localAddress", local);
	        		//String branch = res.getJSONObject(table.getSelectionIndex()).getString("branch");
	        		getClone(uri, local);
	        		close();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        
        cancelBtn.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent evt){
        	//	System.out.println("cancel btn:");
        	//	jgitHelper.push();
        	//	jgitHelper.commit("a new commit. yeah!");
        		close();
        	}
        });
        
		getLocalAddress();
        
		return parent; 

	}
	
	private JSONArray getContent(){
		String token = handler.StringRead("private_token");
		String id = handler.StringRead("id");
		
		JSONArray result = sender.getRequest("/project/get", "private_token=" + token + "&oid=" + id);
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
		String token = handler.StringRead("private_token");
		
		JSONArray result = sender.getRequest("/project/branch", "private_token=" + token + "&id=" + id);
		return result;
	}
	
	private void getLocalAddress(){
		String local = handler.StringRead("localAddress");
		
		localAddressText.setText(handler.StringRead("localAddress"));
		if(local!= null){
			jgitHelper.setFilePath(local);
			remoteAddressText.setText(jgitHelper.getUri());
			confirmBtn.setEnabled(false);
		}
		//remoteAddressText.setText(jgitHelper.getUri());
		//System.out.println("filePath:" + local + " getUri:" + jgitHelper.getUri(local));
	}
	
	private void getClone(String remoteUri, String localPath){
		if(localPath != null && remoteUri != null){
			System.out.println("LocalPath:" + localPath);
			System.out.println("RemoteUri:" + remoteUri);
			jgitHelper.setFilePath(localPath);
			jgitHelper.setRemoteUri(remoteUri);
			handler.StringWrite("localAddress", localPath);
			jgitHelper.gitStart();
		}
		
		//jgitHelper.getInstance(uri, local, branch);
	}
}
