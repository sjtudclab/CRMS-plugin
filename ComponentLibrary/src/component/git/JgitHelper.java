package component.git;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;

public class JgitHelper {
	
	private String filePath = null;
	private String remoteUri = null;
	private Repository repository = null;
	
	public JgitHelper(){}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public void setRemoteUri(String remoteUri){
		this.remoteUri = remoteUri;
	}

	
	
	// 初始化新仓库
	public void createNewRepository() throws IOException, GitAPIException {
		try{
	        // run the init-call
	        File dir = File.createTempFile(filePath, ".test");
	        if(!dir.delete()) {
	            throw new IOException("Could not delete file " + dir);
	        }

	        // The Git-object has a static method to initialize a new repository
	        Git git = Git.init()
	                .setDirectory(dir)
	                .call();
	        System.out.println("Created a new repository at " + git.getRepository().getDirectory());
	        

//	        dir = File.createTempFile("repoinit", ".test");
//	        if(!dir.delete()) {
//	            throw new IOException("Could not delete file " + dir);
//	        }

	        // you can also create a Repository-object directly from the
	        repository = FileRepositoryBuilder.create(new File(dir.getAbsolutePath()));
	        System.out.println("Created a new repository at " + repository.getDirectory());
		} finally {

		}
	}

	//获取仓库 
	public void gitStart(){
		try{
			//如果filePath存在、或者不是目录，或者内有东西，就不能用clone，改用open+fetch
			File file = new File(filePath);
			System.out.println("[gitClone]Start");
			if(!file.exists() || !file.isDirectory() || file.list().length > 0){
				System.out.println("[gitClone]Pulling");
				remoteUri = getUri();
				Git git = Git.open(file);
				PullResult result = git.pull().call();
				git.add().addFilepattern(".").call();
				System.out.println("[gitClone]Pulling from " + remoteUri + " to " + filePath);
			}else{
				System.out.println("[gitClone]Cloning");
				createNewRepository();
				System.out.println("[gitClone]Cloning from " + remoteUri + " to " + filePath);
				Git result = Git.cloneRepository()
					.setURI(remoteUri)
					.setDirectory(file)
					.call();
				System.out.println("[gitClone]Having repository:" + result.getRepository().getDirectory());
			}
		}catch(Exception e){
			System.out.println("[gitClone]" + e.getMessage());
		}catch(Error e){
			e.printStackTrace();
		}
	}
	
	//获取仓库的远端uri
	public String getUri(){
		if(filePath == null) return "";

		File file = new File(filePath, ".git");
		if(!file.exists() || !file.isDirectory() || file.length() == 0)
			return "";
		try {
			Git git = Git.open(file);
			StoredConfig config = git.getRepository().getConfig();
			Set<String> set = config.getSubsections("remote");
			String ss = "";
			for(String s : set){
				ss = config.getString("remote", s, "url");
			}
			if(remoteUri == "")remoteUri = ss;
			return ss;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	//推送
	public void gitPush(String message){
		try{
			//如果filePath存在、或者不是目录，或者内有东西，就不能用推
			File file = new File(filePath);
			System.out.println("[gitPath]Start");
			if(!file.exists() || !file.isDirectory() || file.list().length > 0){
				System.out.println("[gitPush]Pushing");
				remoteUri = getUri();
				Git git = Git.open(file);
				git.commit().setMessage(message).call();
				Iterable<PushResult> result = git.push().call();
				System.out.println("[gitPush]Push to " + remoteUri + " from " + filePath);
			}
		}catch(Exception e){
			System.out.println("[gitPush]" + e.getMessage());
		}catch(Error e){
			e.printStackTrace();
		}
	}
	
}
