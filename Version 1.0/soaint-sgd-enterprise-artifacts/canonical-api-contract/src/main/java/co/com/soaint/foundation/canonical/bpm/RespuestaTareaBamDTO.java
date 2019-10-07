package co.com.soaint.foundation.canonical.bpm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Arce on 9/13/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestatarea/1.0.0")
@ToString
public class RespuestaTareaBamDTO {

    private Long pk;
    private Date eddate;
    private Long duration;
    private Date enddate;
    private Long processinstanceid;
    private Date startdate;
    private String status;
    private Long taskid;
    private String taskname;
    private String userid;
    private Long optlock;
    private Long cantidad;
    private String owner;
    private String description;
    private String deploymentid;


    public RespuestaTareaBamDTO(String status, Long cantidad) {
        this.status = status;
        this.cantidad = cantidad;
    }

    public RespuestaTareaBamDTO(Long pk, Date eddate, Long duration, Date enddate, Long processinstanceid, Date startdate, String status, Long taskid, String taskname, String userid, Long optlock) {
        this.pk = pk;
        this.eddate = eddate;
        this.duration = duration;
        this.enddate = enddate;
        this.processinstanceid = processinstanceid;
        this.startdate = startdate;
        this.status = status;
        this.taskid = taskid;
        this.taskname = taskname;
        this.userid = userid;
        this.optlock = optlock;
    }

    public RespuestaTareaBamDTO (Long taskid, String status,String taskname, Date startdate){
        this.cantidad=taskid;
        this.status=status;
        this.taskname=taskname;
        this.startdate=startdate;
    }

    public RespuestaTareaBamDTO(Long taskid,Long processinstanceid,Date startdate,String description,  String status,  String taskname, String owner, String deploymentid) {
        this.taskid = taskid;
        this.processinstanceid = processinstanceid;
        this.startdate = startdate;
        this.description = description;
        this.status = status;
        this.taskname = taskname;
        this.owner = owner;
        this.deploymentid = deploymentid;
    }
    public RespuestaTareaBamDTO(Long taskid,Long processinstanceid,Date startdate,String description,  String status,  String taskname, String owner) {
        this.taskid = taskid;
        this.processinstanceid = processinstanceid;
        this.startdate = startdate;
        this.description = description;
        this.status = status;
        this.taskname = taskname;
        this.owner = owner;
    }
}



