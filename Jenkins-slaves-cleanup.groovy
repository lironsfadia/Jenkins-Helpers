// Check if a slave has < 10 GB of free space, wipe out workspaces if it does

import hudson.model.*;
import hudson.util.*;
import jenkins.model.*;
import hudson.FilePath.FileCallable;
import hudson.slaves.OfflineCause;
import hudson.node_monitors.*;

for (node in Jenkins.instance.nodes) {
    computer = node.toComputer()
    if (computer.getChannel() == null) continue

    rootPath = node.getRootPath()
    println("rootPath = " + rootPath.getRemote())

    size = DiskSpaceMonitor.DESCRIPTOR.get(computer).size
    roundedSize = size / (1024 * 1024 * 1024) as int
    println("node: " + node.getDisplayName() + ", free space: " + roundedSize + "GB")
    println("NODE MODE = " + node.getMode())
    
    if (roundedSize < 10) {
        computer.setTemporarilyOffline(true, new hudson.slaves.OfflineCause.ByCLI("disk cleanup"))
        
        for(item in Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)){
            println("-------"+item.fullName)

            rootpathAsString = rootPath.getRemote()
            path = node.createPath(rootpathAsString + "\\" + item.fullName)
         
           pathAsString = path.getRemote()

            if (path.exists()) {
                
                isDelete = path.deleteRecursive()
                println(".... deleted from location " + pathAsString)
            } else {
                println(".... nothing to delete at " + pathAsString)
            }
        }
    }

    computer.setTemporarilyOffline(false, null)
}

