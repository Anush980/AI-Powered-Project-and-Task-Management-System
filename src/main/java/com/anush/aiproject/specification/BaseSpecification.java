package com.anush.aiproject.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;


public abstract class BaseSpecification<T> {

    protected Specification<T> equal(String field,Object value){
        return (root,query,cb)->{
            if(value==null) return null;
            return cb.equal(root.get(field), value);
        };
    }

    /**
     * WHERE nested.field = value   (e.g. "user" → "id")
     * Used for ownership checks like WHERE user.id = ?
     */
    protected Specification<T> equalNested(String relation,String field, Object value){
        return (root,query,cb)->{
            if (value ==null) return null;
            return cb.equal(root.get(relation).get(field), value);
        };
    }

     /**
     * WHERE LOWER(field) LIKE %value%
     * Case-insensitive search. Skipped if value is null or blank.
     */
    protected Specification<T> like (String field,String value){
        return (root,query,cb)->{
            if(value==null || value.isBlank()) return null;
            return cb.like(
                cb.lower(root.get(field)),
                "%"+value.toLowerCase().trim()+"%"
                );
        };
    }

    /**
     * WHERE field1 LIKE %value% OR field2 LIKE %value%
     * Search across multiple text columns at once.
     */

        protected Specification<T> likeAny(String value, String... fields) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) return null;
            String pattern = "%" + value.toLowerCase().trim() + "%";
            Predicate[] predicates = new Predicate[fields.length];
            for (int i = 0; i < fields.length; i++) {
                predicates[i] = cb.like(cb.lower(root.get(fields[i])), pattern);
            }
            return cb.or(predicates);
        };
    }
    /**
     * WHERE field >= value
     */
    protected <C extends Comparable<C>> Specification<T> greaterThanOrEqual(String field, C value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            return cb.greaterThanOrEqualTo(root.get(field), value);
        };
    }

    /**
     * WHERE field <= value
     */
    protected <C extends Comparable<C>> Specification<T> lessThanOrEqual(String field, C value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            return cb.lessThanOrEqualTo(root.get(field), value);
        };
    }

    /**
     * WHERE field IN (value1, value2, ...)
     */
    protected Specification<T> in(String field, java.util.Collection<?> values) {
        return (root, query, cb) -> {
            if (values == null || values.isEmpty()) return null;
            return root.get(field).in(values);
        };
    }

    /**
     * Helper — combines a list of specs with AND.
     * Null specs (skipped filters) are ignored automatically.
     */
    protected Specification<T> combine(Specification<T>... specs) {
        Specification<T> result = Specification.where(null);
        for (Specification<T> spec : specs) {
            result = result.and(spec);
        }
        return result;
    }
}
