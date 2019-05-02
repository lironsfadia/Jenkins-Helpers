def filePath = build.getWorkspace().toString()+"\\file_res.props"

println "filePath =$filePath"
channel = build.getWorkspace().channel;
fp = new FilePath(channel, filePath)

if(fp != null)
{
    fp.write("RES=$res\n", null)
}
