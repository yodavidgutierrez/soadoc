package co.com.soaint.bpm.services.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASK")
@NamedQueries({
        @NamedQuery(name = "TaskEntity.delegate", query = "update" +
                " TaskEntity task set task.actualowner_id =:actualowner_id, task.createdby_id =:actualowner_id " +
                "WHERE task.id=:id"),
        @NamedQuery(name = "TaskEntity.getTaskByID", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO" +
                " (task.actualowner_id,task.id) "+
                "FROM TaskEntity task " +
                "WHERE task.id=:id"),
        @NamedQuery(name = "TaskEntity.inProgress", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO" +
                " (task.id,task.status,task.taskname,task.createdon) " +
                "FROM TaskEntity task " +
                "WHERE TRIM(task.actualowner_id)=trim(:USUARIO) AND TRIM(task.status)=trim(:STATUS)"),
        @NamedQuery(name = "TaskEntity.findAllByUser", query = "SELECT task " +
                "FROM TaskEntity task " +
                "WHERE task.actualowner_id=:USUARIO and (task.status=:RESERVERD OR task.status=:INPROGESS )"),
        @NamedQuery(name = "TaskEntity.ready", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO" +
                " (task.id,task.status,task.taskname,task.createdon) " +
                "FROM TaskEntity task " +
                "WHERE TRIM(task.actualowner_id)=trim(:USUARIO) AND TRIM(task.status)=trim(:STATUS)"),
        @NamedQuery(name = "TaskEntity.readyWithoutUser", query = "SELECT task " +
                "FROM TaskEntity task " +
                "WHERE  TRIM(task.status)=trim(:STATUS)")
})

public class TaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private Long id;
    @Basic
    @Column(name = "ACTUALOWNER_ID")
    private String actualowner_id;
    @Basic
    @Column(name = "CREATEDBY_ID")
    private String  createdby_id;
    @Basic
    @Column(name = "STATUS")
    private String status;
    @Basic
    @Column(name = "NAME")
    private String taskname;
    @Basic
    @Column(name = "CREATEDON")
    @Temporal(TemporalType.DATE)
    private Date    createdon;
    @Basic
    @Column(name = "PROCESSINSTANCEID")
    private Long    processinstanceid;
    @Basic
    @Column(name = "DESCRIPTION")
    private String  description;
    @Basic
    @Column(name = "DEPLOYMENTID")
    private String  deploymentid;
    @Basic
    @Column(name = "PROCESSID")
    private String  processid;
    @Basic
    @Column(name = "ACTIVATIONTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationtime;
    @Basic
    @Column(name = "EXPIRATIONTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date    expirationtime;
}
