package edu.hm.hafner.warningsngui.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="build")
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String _class;
    private int number;
    private String url;

    @ManyToOne
    private Job job;

    @OneToMany(mappedBy = "build", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Tool> tools = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}