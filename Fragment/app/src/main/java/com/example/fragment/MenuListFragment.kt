package com.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)

        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)

        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()

        // リストデータ(MutableMap)の作成
        // リストデータ(MutableMap)の定義
        var menu = mutableMapOf("name" to "item 1", "price" to "$8")
        // MutableListへの追加
        menuList.add(menu)
        // 以降リストデータの定義・リストへの追加
        menu = mutableMapOf("name" to "item 2", "price" to "$7")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 3", "price" to "$2")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 4", "price" to "$6")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 5", "price" to "$12")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 6", "price" to "$20")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 7", "price" to "$9")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 8", "price" to "$8")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 9", "price" to "$3")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 10", "price" to "$4")
        menuList.add(menu)
        menu = mutableMapOf("name" to "item 11", "price" to "$25")
        menuList.add(menu)

        val from = arrayOf("name", "price")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleAdapter(
            activity,
            menuList,
            android.R.layout.simple_list_item_2,
            from,
            to
        )
        lvMenu.adapter = adapter

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                MenuListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}