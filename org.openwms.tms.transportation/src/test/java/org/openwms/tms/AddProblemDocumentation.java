/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.tms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.openwms.tms.api.CreateTransportOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

/**
 * A AddProblemDocumentation.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class AddProblemDocumentation extends DocumentationBase {

    @Autowired
    private EntityManager em;

    public
    @Test
    void testAddProblem() throws Exception {
        // setup ...
        CreateTransportOrderVO vo = createTO();
        MvcResult res = postTOAndValidate(vo, NOTLOGGED);
        Message msg = new Message.Builder().withMessage("text").withMessageNo("77").build();
        vo.setProblem(msg);

        // test ...
        mockMvc.perform(
                patch(TMSConstants.ROOT_ENTITIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vo))
                )
                .andExpect(status().isNoContent())
                .andDo(document("to-patch-addproblem"))
        ;

        em.flush();
        TransportOrder saved = em.createQuery("select to from TransportOrder to where to.pKey = :pkey", TransportOrder.class).setParameter("pkey", vo.getpKey()).getSingleResult();
        assertThat(saved.getProblem()).isEqualTo(msg);
    }
}
