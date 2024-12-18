package com.fuzzy.search.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_TEMP", schema = "SCAT", catalog = "")
public class ProductTempModel extends AbstractAuditingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ")
    @SequenceGenerator(name = "PRODUCT_SEQ", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Id
    @Column(name = "PRODUCT_ID", nullable = false)
    private Integer productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

}
