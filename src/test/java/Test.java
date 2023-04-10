import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        List<String> lines = FileUtil.readLines("E:\\xiaopeng\\cloud-nsrm-extend\\biz\\cloud-biz-sou-extend\\sou-base\\src\\main\\java\\com\\midea\\cloud\\srm\\sou\\sourcing\\control\\judge\\AbstractSouControlJudge.java", StandardCharsets.UTF_8);
        Set<String> assertMsg = new HashSet<>();
        for (String line : lines) {
            if (line.contains("Assert")) {
                List<String> all = ReUtil.findAll("\"(.*?)\"", line, 0);
                assertMsg.addAll(all);
            }
        }
        System.out.println(assertMsg);
    }
}
