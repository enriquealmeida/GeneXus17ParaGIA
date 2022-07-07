package com.genexus.android.controls.grids.smart;

import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.grids.GridHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RecyclerViewAdapterTest {
    private RecyclerViewAdapter mAdapter;

    @Before
    public void createView() {
        GridDefinition definition = mock(GridDefinition.class);
        GxRecyclerView recyclerView = mock(GxRecyclerView.class);
        GridHelper helper = mock(GridHelper.class);
        mAdapter = new RecyclerViewAdapter(definition, helper, recyclerView);
        setData(new EntityList());
    }

    private void setData(EntityList entities) {
        ViewData data = mock(ViewData.class);
        when(data.getEntities()).thenReturn(entities);
        mAdapter.setData(data);
    }

    @Test
    public void itemCountZero() {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
    }

    @Test
    public void itemCountTwo() {
        EntityList entities = new EntityList();
        Entity entity = mock(Entity.class);
        entities.addEntity(entity);
        entities.addEntity(entity);
        setData(entities);
        assertThat(mAdapter.getItemCount()).isEqualTo(2);
    }
}
