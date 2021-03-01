package com.example.backend.test.inspection;

import com.example.backend.test.question.QuestionServcice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    @Autowired
    InspectionServcice inspectionServcice;

    @GetMapping
    public ResponseEntity<List> getInspectionList() throws  Exception{

        return new ResponseEntity<>(inspectionServcice.getInspectionList(), HttpStatus.OK);
    }


    @GetMapping("/{idx}")
    public ResponseEntity<Inspection> getInspectionDetail(@PathVariable int idx){

        Inspection inspectionDetail = inspectionServcice.getInspectionDetail(idx);
        if(inspectionDetail == null){
            //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Inspection>(inspectionServcice.getInspectionDetail(idx), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateInspection(@RequestBody Inspection inspection){
        return null;
    }

}