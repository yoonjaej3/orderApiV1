package org.jyj.order.entity;

import lombok.*;
import org.jyj.common.utils.BaseEntity;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_OUT_ORD_MASTER")
public class OutOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "order_dt")
    private String orderDt;

    @Column(name = "wh_cd")
    private String whCd;

    @Column(name = "orderer_nm")
    private String ordererNm;

    @Column(name = "orderer_address")
    private String ordererAddress;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "outOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutOrderItem> OutOrderItems;

    public void makeOrdNo(double seq) {
        DecimalFormat df = new DecimalFormat("00000");
        String orderNo = this.whCd + this.orderDt + df.format(seq);

        this.orderNo = orderNo;

    }

}
