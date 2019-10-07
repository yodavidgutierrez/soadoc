package co.com.foundation.soaint.documentmanager.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrodriguez on 25/10/2016.
 */
public class OrganigramaTreeVO {

    private TreeCoreVO core;
    private TreeCheckboxVO checkbox;
    private List<String> plugins;

    public OrganigramaTreeVO(TreeCoreVO core, TreeCheckboxVO checkbox,  List<String> plugins) {
        this.core = core;
        this.checkbox =checkbox;
        this.plugins =plugins;
    }

    public static OrganigramaTreeVO newInstance(List<OrganigramaItemVO> data){
        TreeCoreVO treeCoreVO =new TreeCoreVO(data, false);
        TreeCheckboxVO checkboxVO =new TreeCheckboxVO(false, false);

        List<String> plugins =new ArrayList<>();
        plugins.add("checkbox");
        plugins.add("search");

        return new OrganigramaTreeVO(treeCoreVO, checkboxVO, plugins);
    }

    public TreeCoreVO getCore() {
        return core;
    }

    public List<OrganigramaItemVO> getData() {
        return core.getData();
    }

    public TreeCheckboxVO getCheckbox() {
        return checkbox;
    }

    public List<String> getPlugins() {
        return plugins;
    }
}

class TreeCoreVO{
    private List<OrganigramaItemVO> data;
    private boolean multiple;
    public TreeCoreVO(List<OrganigramaItemVO> data, boolean multiple) {
        this.data = data;
        this.multiple = multiple;
    }
    public List<OrganigramaItemVO> getData() {
        return data;
    }

    public boolean getMultiple() {
        return multiple;
    }
}

class TreeCheckboxVO{

    private boolean keep_selected_style;
    private boolean three_state;
    public TreeCheckboxVO(boolean keep_selected_style, boolean three_state) {
        this.keep_selected_style = keep_selected_style;
        this.three_state = three_state;
    }
    public boolean getKeep_selected_style() {
        return keep_selected_style;
    }
    public boolean getThree_state() {
        return three_state;
    }
}

