package ${basePackageName}.api.controller.${moduleName};

import ${basePackageName}.common.domain.entity.${entityClassName};
import ${basePackageName}.persistence.service.${entityClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.ranran.common.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.framework.context.PageContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ranran.common.domain.Result;
import org.springframework.validation.annotation.Validated;
import com.ranran.framework.web.controller.BaseController;

/**
* ${functionName}Controller
* @author ${author}
*/
@RestController
@RequestMapping("/api/${entityClassName?lower_case}")
public class ${entityClassName}Controller extends BaseController {

    @Autowired
    private ${entityClassName}Service ${entityClassName?uncap_first}Service;

    /**
    * 分页查询${entityClassName}数据
    */
    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('${moduleName?lower_case}:${businessName}:index')")
    public PageVo page(${entityClassName} ${entityClassName?uncap_first}) {
        Page<${entityClassName}> page = PageContext.getPage();
        IPage<${entityClassName}> dataPage = ${entityClassName?uncap_first}Service.page(page);
        return getPageData(dataPage.getRecords(), dataPage.getTotal());
    }

    /**
    * 新增${entityClassName}数据
    */
    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('${moduleName?lower_case}:${businessName}:index')")
    public Result<Void> add(@Validated @RequestBody ${entityClassName} ${entityClassName?uncap_first}) {
        boolean save = ${entityClassName?uncap_first}Service.save(${entityClassName?uncap_first});
        if (save) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }

    /**
    * 修改${entityClassName}数据
    */
    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('${moduleName?lower_case}:${businessName}:index')")
    public Result<Void> edit(@Validated @RequestBody ${entityClassName} ${entityClassName?uncap_first}) {
        boolean update = ${entityClassName?uncap_first}Service.updateById(${entityClassName?uncap_first});
        if (update) {
            return Result.success();
        } else {
            return Result.fail("修改失败");
        }
    }

    /**
    * 根据ID查询单个${entityClassName}数据
    */
    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('${moduleName?lower_case}:${businessName}:index')")
    public Result<${entityClassName}> getById(@RequestParam Long ${entityClassName?uncap_first}Id) {
            ${entityClassName} ${entityClassName?uncap_first} = ${entityClassName?uncap_first}Service.getById(${entityClassName?uncap_first}Id);
        return Result.success(${entityClassName?uncap_first});
    }

    /**
    * 根据ID删除${entityClassName}数据
    */
    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('${moduleName?lower_case}:${businessName}:index')")
    public Result<Void> delete(@RequestParam Long ${entityClassName?uncap_first}Id) {
        boolean remove = ${entityClassName?uncap_first}Service.removeById(${entityClassName?uncap_first}Id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }
}
