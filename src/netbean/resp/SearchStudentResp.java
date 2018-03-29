package netbean.resp;

import java.util.ArrayList;

public class SearchStudentResp {
    public int totalPage;
    public int limit;
    public ArrayList<StudentInfo> studentInfos;

    public SearchStudentResp(){
        studentInfos = new ArrayList<>();
    }

}
