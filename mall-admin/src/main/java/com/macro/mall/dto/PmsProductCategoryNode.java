package com.macro.mall.dto;

import com.macro.mall.model.PmsProductCategory;
import lombok.Data;

import java.util.List;

@Data
public class PmsProductCategoryNode extends PmsProductCategory {
    private List<PmsProductCategoryNode> children;
}
