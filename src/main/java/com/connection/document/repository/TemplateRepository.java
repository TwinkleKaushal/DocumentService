package com.connection.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.connection.document.model.TemplateModel;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateModel, Integer>{

	public TemplateModel findByTemplateType(String templateType);
	
}
