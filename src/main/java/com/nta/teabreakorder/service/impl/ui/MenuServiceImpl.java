package com.nta.teabreakorder.service.impl.ui;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.common.DaoUtils;
import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.ui.Menu;
import com.nta.teabreakorder.repository.ui.MenuRepository;
import com.nta.teabreakorder.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public ResponseEntity get(Pageable pageable) throws Exception {
        List<Menu> menuList = menuRepository.findAllByParentMenuIsNullOrderBySort();
        if(pageable.getSearchData()!=null){
            Map<String, String> mapSearch = DaoUtils.getSearchDataFromParam(pageable.getSearchData());
            String[] roles = mapSearch.get("roles").split(",");
            Set<String> result;
            menuList = menuList.stream().filter(m -> {
                if (m.getRoles().isEmpty()) return true;
                long count = Arrays.stream(roles).distinct()
                        .filter(m.getRoles()::contains).count();
                return count > 0;
            }).collect(Collectors.toList());
        }
        return CommonUtil.createResponseEntityOK(menuList);
    }

    @Override
    public ResponseEntity getById(Long id) throws Exception {
        return CommonUtil.createResponseEntityOK(menuRepository.findById(id));
    }

    @Override
    public ResponseEntity create(Menu menu) throws Exception {
        if (menu.getParentMenu() != null) {
            Menu parentMenu = menuRepository.getById(menu.getParentMenu().getId());
            menu.setParentMenu(parentMenu);
        }
        return CommonUtil.createResponseEntityOK(menuRepository.save(menu));
    }

    @Override
    public ResponseEntity update(Menu menu) throws Exception {
        Menu oldMenu = menuRepository.getById(menu.getId());
        oldMenu.setRoles(menu.getRoles());
        oldMenu.setIcon(menu.getIcon());
        oldMenu.setPath(menu.getPath());
        oldMenu.setTitle(menu.getTitle());
        oldMenu.setActivated(menu.isActivated());
        oldMenu.setSort(menu.getSort());
        return CommonUtil.createResponseEntityOK(menuRepository.save(oldMenu));
    }

    @Override
    public ResponseEntity deletes(List<Long> ids) throws Exception {
        menuRepository.deleteAllById(ids);
        return CommonUtil.createResponseEntityOK(1);
    }
}
