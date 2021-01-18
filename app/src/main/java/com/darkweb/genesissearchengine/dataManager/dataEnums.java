package com.darkweb.genesissearchengine.dataManager;

public class dataEnums
{
    /*Settings Manager*/
    public enum eHistoryCommands {
        M_GET_HISTORY, M_ADD_HISTORY, M_REMOVE_HISTORY, M_CLEAR_HISTORY, M_GET_SUGGESTIONS, M_LOAD_MORE_HISTORY, M_INITIALIZE_HISTORY, M_HISTORY_SIZE
    }

    public enum eBookmarkCommands {
        M_ADD_BOOKMARK, M_GET_BOOKMARK, M_DELETE_BOOKMARK, M_CLEAR_BOOKMARK, M_GET_SUGGESTIONS;
    }

    public enum ePreferencesCommands{
        M_SET_STRING, M_SET_BOOL, M_SET_INT, M_SET_FLOAT, M_GET_STRING, M_GET_BOOL, M_GET_INT, M_GET_FLOAT, M_CLEAR_PREFS
    }

    public enum eSuggestionCommands{
        M_UPDATE_SUGGESTION, M_CLEAR_SUGGESTION, M_INIT_SUGGESTION, M_ADD_SUGGESTION, M_GET_SUGGESTION
    }

    public enum eTabCommands{
        M_UPDATE_TAB, M_ADD_TAB, M_CLEAR_TAB,M_GET_SUGGESTIONS, CLOSE_TAB, GET_TAB, GET_CURRENT_TAB, GET_LAST_TAB, GET_TOTAL_TAB, MOVE_TAB_TO_TOP, M_UPDATE_PIXEL, M_HOME_PAGE
    }

    public enum eImageCommands{
        M_REQUEST_IMAGE_URL, M_GET_IMAGE
    }
}