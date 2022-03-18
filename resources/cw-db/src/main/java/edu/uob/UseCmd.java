package edu.uob;

public class UseCmd extends DBCommands  {

    public UseCmd() {
    }

    @Override
    public String query(DBServer s) {
        return "super.query(s)";
    }
}







//    private int index;
//    private final String databaseName;
//    private final StorageType type;
//
//    public UseCMD(ArrayList<String> command, int index) throws DBException {
//        this.index = index;
//        type = StorageType.DATABASE;
//        databaseName = parseDatabaseName(command, index);
//        this.index += 2;
//        //increase index to be pointing to the ; after databaseName
//    }
//
//    public String getDatabaseName() throws EmptyData {
//        if(databaseName!=null) {
//            return databaseName;
//        }
//        throw new EmptyData("database name");
//    }
//
//    public int getIndex(){
//        return index;
//    }
//
//    public StorageType getType(){
//        return type;
//    }
//}
