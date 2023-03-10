package com.greenpoint.server.store.service;

import com.greenpoint.server.exception.GlobalException;
import com.greenpoint.server.point.service.PointService;
import com.greenpoint.server.store.model.Store;
import com.greenpoint.server.store.model.StoreClientResponse;
import com.greenpoint.server.store.model.StoreRequest;
import com.greenpoint.server.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PointService pointService;


    @Transactional(readOnly = true)
    public Store findById(Long storeId) {
        Store ret = storeRepository.findById(storeId).get();
        return ret;
    }

    @Transactional(readOnly = true)
    public List<StoreClientResponse> findAll() {
        List<Store> stores = storeRepository.findAll();
        List<StoreClientResponse> ret = stores.stream().map(StoreClientResponse::from).collect(Collectors.toList());
        for(int i=0; i<ret.size(); i++){
            int max = pointService.findMaxPointByStore(ret.get(i).getStoreId());
            ret.get(i).maxp(max);
        }
        return ret.stream().sorted(Comparator.comparing(StoreClientResponse::getMaximumPoint).reversed()).collect(Collectors.toList());
    }


    @Transactional
    public Store create(Store store) {
        Store ret = storeRepository.save(store);
        return ret;
    }

    @Transactional
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    @Transactional
    public Store update(Long id, StoreRequest request) {
        Store ret = this.findOne(id);
        ret.update(request);
        return ret;
    }

    private Store findOne(Long id) {
        return storeRepository.findById(id).get();
    }


//    public List<Store> findThreeById(Long id) {
//        List<Store> ret = storeRepository.findThreeById(id);
//        return ret;
//    }
}
